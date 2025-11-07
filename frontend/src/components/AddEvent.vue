<script setup>
import axios from "axios";
import { ref } from "vue";
import {token} from "@/auth.js";

const date = ref(null);
const time = ref(null);
const meetingPoint = ref(null);
const city = ref(null)  //existing enum
const distance = ref(null);
const pace = ref(null);
const club = ref(null)  //from user's created clubs
const description = ref(null);
const message = ref(null);
const event = ref({})

const cities = ref([])
const loadingCities = ref(true)
const errorCities = ref(null)
const clubs = ref([])
const loadingClubs = ref(true)
const errorClubs = ref(null)

const modalOpen = ref(false)

const fetchCities = async() =>{
  try {
    const res = await axios.get('http://192.168.1.128:8080/cities')
    cities.value = res.data

  } catch (err) {
    console.error('Failed to load cities:', err)
    errorCities.value = "Error to load cities"
  } finally{
    loadingCities.value = false
  }
}


const fetchOwnedClubs = async() =>{
  if(token.value){
    try {
      const res = await axios.get('http://192.168.1.128:8080/clubs/owned-clubs', {
        headers: {
          Authorization: `Bearer ${token.value}`
        }
      })
      clubs.value = res.data

    } catch (err) {
      console.error('Failed to load clubs', err)
      errorClubs.value = "Error to load clubs"
    } finally {
      loadingClubs.value = false
    }
  }
}



const addEvent = async () => {
  event.value = {
    distance: Number(distance.value),
    pace: Number(pace.value),
    meetingPoint: meetingPoint.value,
    city: city.value,
    club: club.value,
    eventDescription: description.value,
    dateTime: `${date.value}T${time.value}:00+02:00`,
  };
  console.log(event.value)

  try {
    //spanish time
    event.value.dateTime = `${date.value}T${time.value}:00+02:00`;

    const response = await axios.post("http://localhost:8080/events/owned-events", event.value,
        {headers: {
        Authorization: `Bearer ${token.value}`
      },
    })
    message.value = `"${response.data.distance}"km run added successfully!`;

    // reset form
    date.value = '';
    time.value = '';
    distance.value = '';
    pace.value = '';
    meetingPoint.value = '';
    city.value = '';
    club.value = '';
    description.value = '';
    event.value = {};

  } catch (error) {
    console.error(error);
    message.value = "Error adding event.";
  }
};
</script>

<template>
  <div class="container">
    <h2 v-if="!modalOpen">+ Add  a new event</h2>

    <div v-else>
      <form @submit.prevent="addEvent">
        <div>
          <label>Date</label>
          <input type="date" v-model="date" />
        </div>

        <div>
          <label>Time</label>
          <input type="time" v-model="time" step="300"/>
        </div>

        <div>
          <label>Distance</label>
          <textarea v-model="distance"></textarea>
        </div>

        <div>
          <label>Pace</label>
          <textarea v-model="pace"></textarea>
        </div>

        <div>
          <label>Meeting Point</label>
          <textarea v-model="meetingPoint"></textarea>
        </div>

        <div>
          <label>City</label>
          <textarea v-model="city"></textarea>
        </div>

        <div>
          <label>Club</label>
          <select v-model="club">
            <option
                v-for="club in clubs"
                :key="club.id"
                :value="club">
              {{ club.name }}
            </option>
          </select>
        </div>

        <div>
          <label>Description</label>
          <textarea v-model="description"></textarea>
        </div>


        <button type="submit">
          Add Event
        </button>
      </form>

      <p v-if="message">{{ message }}</p>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  padding: 1.5rem;
  margin: 1rem;


  color: rgba(255, 255, 255, 0.8);  /* soft off-white text */
  background: linear-gradient(180deg, rgba(46,139,255,0.12), rgba(24,24,24,1));
  border: 1px dashed rgba(46, 139, 255, 0.4);
  font-weight: 500;
  cursor: pointer;
  border-radius: 10px;
  transition: box-shadow 0.2s ease;
}

.container:hover {
  box-shadow: 0 0 12px rgba(46, 139, 255, 0.2);
}



</style>


