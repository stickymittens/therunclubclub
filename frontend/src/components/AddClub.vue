<script setup>
  import axios from "axios";
  import { ref } from "vue";
  import addEvent from "@/components/AddEvent.vue";

  const clubName = ref('');
  const clubDescription = ref('');
  const message = ref('');


  const club = ref({
    clubName: clubName.value,
    clubDescription: clubDescription.value
  });

  const addClub = async () => {
    try {
      const response = await axios.post("http://localhost:8080/clubs", club.value)
      message.value = `The "${response.data.clubName}" running club added successfully!`;

      // reset form
      clubName.value = '';
      clubDescription.value = '';


      club.value = {
        clubName: '',
        clubDescription: ''
      };
    } catch (error) {
      console.error(error);
      message.value = "Error adding event.";
    }
  };
</script>

<template>
  <div class="container">
    <h2 >Add New Running CLub</h2>

    <form @submit.prevent="addEvent">
      <div>
        <label>Name</label>
        <input v-model="clubName" />
      </div>

      <div>
        <label>Avatar </label>
        <input v-model="clubDescription"/>
      </div>

      <button type="submit">
        Add Event
      </button>
    </form>

    <p v-if="message">{{ message }}</p>
  </div>
</template>