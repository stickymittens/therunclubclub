<script setup>
  import axios from "axios";
  import { ref } from "vue";
  import {token} from "@/auth.js";

  const clubName = ref(null);
  const clubDescription = ref(null);
  const message = ref('');
  const club = ref({})

  const addClub = async () => {
    club.value = {
      clubName: clubName.value,
      clubDescription: clubDescription.value
    };
    console.log(club.value)

    if(token.value){
      try {
        const response = await axios.post("http://localhost:8080/clubs", club.value,
            {
              headers: {
                Authorization: `Bearer ${token.value}`,
              },
            })
        message.value = `The "${response.data.clubName}" running club added successfully!`;

        // reset variables
        clubName.value = null;
        clubDescription.value = null;

        //reset ref
        club.value = {
          clubName: null,
          clubDescription: null
        };
      } catch (error) {
        console.error(error);
        message.value = "Error adding event.";
      }
    }
  };
</script>

<template>
  <div class="container">
    <h2 >Add New Running Club</h2>

    <form @submit.prevent="addClub">
      <div class="label-input">
        <label>Name</label>
        <input v-model="clubName" />
      </div>

      <div class="label-input">
        <label >Description</label>
        <input v-model="clubDescription"/>
      </div>

      <button type="submit" class="submit-btn">
        Add Club
      </button>
    </form>

    <p v-if="message">{{ message }}</p>
  </div>
</template>

<style scoped>
.container{
  font-family: 'Inter', sans-serif;
  display: flex;
  flex-direction: column;
  background: none;
}

.label-input{
  display: flex;
  justify-content: space-between;
}

.submit-btn {
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

.submit-btn:hover {
  background-color: #ff6a3c;
}

</style>