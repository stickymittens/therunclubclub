<script setup>
import {ref, onMounted, computed, watch} from 'vue'
  import axios from 'axios'
  import { useRoute } from 'vue-router'
  import { jwtDecode } from "jwt-decode";
  import {token} from "@/auth.js";

  const route = useRoute()
  const event = ref(null)
const userSignedUp = ref(false)

  const loading = ref(true)
  const error = ref(null)

  const username = ref(null);
  const message = ref("");

  const eventData = ref(null)
 const totalDistance = ref(null)

  // format time
  function formatTime(dateTime) {
    return new Date(dateTime).toLocaleTimeString([], {
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  //format date
function formatDate(dateTime) {
  const date = new Date(dateTime)
  const day = date.getDate()
  const month = date.getMonth() + 1
  return `${day}.${month}`
}

  //format pace
  function formatPace(pace) {
    const minutes = Math.floor(pace)
    const seconds = Math.round((pace - minutes) * 60)
    return `${minutes}:${seconds.toString().padStart(2, '0')}`
  }

  //fetching event by id
  async function fetchEvent(id) {
    try {
      const res = await axios.get(`http://192.168.1.128:8080/events/${id}`)
      event.value = res.data
    } catch (err) {
      console.error('Error fetching event:', err)
    }finally {
      loading.value = false
    }
  }

const fetchClubData = async(clubName) =>{
  try {
    const res = await axios.get(`http://192.168.1.128:8080/events/by-club?clubName=${clubName}`)
    eventData.value = res.data

    totalDistance.value = eventData.value.reduce((sum, event) => sum + event.distance, 0)

    console.log('Total distance for this club:', totalDistance.value, 'km')
  } catch (err) {
    console.error('Error fetching event:', err)
  }finally {
    loading.value = false
  }
}

  //get username if user logged in
  function decodeToken() {
    if (token.value) {
      try {
        const decoded = jwtDecode(token.value);
        username.value = decoded.sub  || null;
      } catch (err) {
        console.error("Invalid token", err);
        username.value = null;
      }
    } else {
      username.value = null;
    }
  }

  const checkIfUserSignedUp = () => {
    if(event.value){
      if(event.value.signedUpUsers.length > 0){
        const signedUpUsers = event.value.signedUpUsers;
        userSignedUp.value = false;

        for (let i = 0; i < signedUpUsers.length; i++) {
          if (username.value === signedUpUsers[i].username) {
            userSignedUp.value = true;
            break;
          }
        }
        return userSignedUp.value;
      }
    }
  };

  const joinEvent = async (id) => {
    try {
      if(token.value){
        await axios.post(
            `http://localhost:8080/events/${id}/sign-up`,
            null,
            {
              headers: {
                Authorization: `Bearer ${token.value}`,
              },
            })
        message.value = `Added successfully!`;
      }

      await fetchEvent(event.value.id)
      checkIfUserSignedUp()

    } catch (error) {
      console.error(error);
      message.value = "Error signing up";
    }
  };

const leaveEvent = async (id) => {
  try {
    if (token.value) {
      await axios.delete(
          `http://localhost:8080/events/${id}/leave`,
          {
            headers: {
              Authorization: `Bearer ${token.value}`,
            },
          }
      )
      message.value = `Left successfully!`

      event.value.signedUpUsers = event.value.signedUpUsers.filter(
          user => user.username !== username.value
      )
      userSignedUp.value = false
    }

    await fetchEvent(event.value.id)
    checkIfUserSignedUp()

  } catch (error) {
    console.error(error)
    message.value = "Error leaving"
  }
}


watch(token, () => {
  decodeToken();
});

watch(() => route.params.id, async (newId) => {
  if (newId) {
    await fetchEvent(newId);
    checkIfUserSignedUp();
  }
});

onMounted(async () => {
  decodeToken()

  const id = route.params.id
  if (id) {
    await fetchEvent(id)
    checkIfUserSignedUp()
    if (event.value?.club?.clubName) {
      await fetchClubData(event.value.club.clubName)
    }
  }
})
</script>

<template>
  <div class="container">

    <div v-if="event">
      <div v-if="loading">Loading event...</div>
      <div v-else-if="error" class="error">{{ error }}</div>

      <div v-else>
        <div class="profile-head">
          <div class="title">
            <h1>{{event.club.clubName}}</h1>
            <p>{{formatDate(event.dateTime)}}</p>
          </div>

          <div class="club-data">
            <p>We have run {{ totalDistance }}km together - join the club</p>
          </div>
        </div>

        <div class="body-container">
          <div class="run-data">
            <p>{{ formatTime(event.dateTime) }}</p>
            <p>-</p>
            <p>{{ event.distance }}km</p>
            <p>-</p>
            <p >{{ formatPace(event.pace) }}min/km</p>
          </div>

          <div class="run-map-container">
            <p>MEETING POINT</p>
            <img src="https://i.sstatic.net/PmTVQ.png" alt="location-map" class="run-map"/>

            <div v-if="username" class="run-btn">
              <button v-if="userSignedUp" @click="leaveEvent(event.id)" class="join-btn leave-btn">Leave the run</button>
              <button v-else ref="joinRun" @click="joinEvent(event.id)" class="join-btn">Join the run</button>
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container{
  position: relative;

  display: flex;
  flex-direction: column;
  gap: 2rem;

  width: 100%;
}

.profile-head{
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 20vh;

  background-color: #000000;
  border-bottom: 1px solid #FB5624;

  padding: 1rem;
}

.title{
  text-transform: uppercase;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
}

.title p{
  font-size: 2rem;
}

.club-data{
  color: white;
  display: flex;
  gap: 1rem;
  font-size: 12px;
  font-weight: 200;

}

.body-container{
  padding: 2rem 1rem;

  display: flex;
  flex-direction: column;
  gap: 7vh;

  font-size: 16px;
}

.run-data{
  display: flex;
  width: 100%;
  justify-content: space-between;
}

.run-map-container{
  font-weight: 800;
  display: flex;
  flex-direction: column;
  gap: 1rem;

  padding: 0 2rem;

  width: 100%;
}

.run-map{
  width:  100%;
}

.run-btn{
  align-self: flex-end;
  font-size: 16px;
}

.join-btn {
  margin-top: 0.4rem;
  background-color: #fb5624;
  border: none;
  border-radius: 6px;
  color: white;
  font-weight: 600;
  font-size: 0.85rem;
  padding: 0.4rem 0.8rem;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.join-btn:hover {
  background-color: #ff6a3c;
}

.leave-btn{
  background-color: #2E8BFF;
}

.leave-btn:hover {
  background-color: #5CA5FF;
}

</style>