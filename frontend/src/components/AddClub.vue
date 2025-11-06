<script setup>
  import axios from "axios";
  import { ref } from "vue";
  import addEvent from "@/components/AddEvent.vue";
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
    <h2 >Add New Running CLub</h2>

    <form @submit.prevent="addClub">
      <div>
        <label>Name</label>
        <input v-model="clubName" />
      </div>

      <div>
        <label>Description</label>
        <input v-model="clubDescription"/>
      </div>

      <button type="submit">
        Add Club
      </button>
    </form>

    <p v-if="message">{{ message }}</p>
  </div>
</template>