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
            {headers: {
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
    <h2>Add a new running club</h2>

    <form @submit.prevent="addClub" class="form-container">
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
.container {
  font-family: 'Inter', sans-serif;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 1.5rem;

  color: rgba(255, 255, 255, 0.8);  /* soft off-white text */
  background: linear-gradient(180deg, rgba(251,86,36,0.12), rgba(24,24,24,1));
  border: 1px dashed rgba(251, 86, 36, 0.4);
  font-weight: 500;
  cursor: pointer;
  margin: 1rem;
  border-radius: 10px;
  transition: box-shadow 0.3s ease;
}

.container:hover {
  box-shadow: 0 0 12px rgba(251, 86, 36, 0.2);
}

h2{
  font-size: 24px;
  text-align: left;
}

.form-container {
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
  width: 100%;
}

/* Arrange label + input side by side */
.label-input {
  display: flex;
  justify-content: space-between;
  text-align: left;
  gap: 0.8rem;
}


.label-input label {
  flex: 0 0 35%;                 /* fixed width label (adjust as needed) */
  font-size: 0.8rem;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.7);
  white-space: nowrap;
}


.label-input input {
  flex: 1;                       /* takes remaining width */
  background-color: #111;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 6px;
  padding: 0.5rem 0.6rem;
  color: rgba(255, 255, 255, 0.9);
  font-size: 0.85rem;
  transition: border 0.2s ease, box-shadow 0.2s ease, background 0.2s ease;
  outline: none;
  box-sizing: border-box;
  min-width: 0;                  /* prevents flex overflow */
}

.label-input input:hover {
  border-color: rgba(251, 86, 36, 0.3);
}

.label-input input:focus {
  border-color: #FB5624;
  box-shadow: 0 0 6px rgba(251, 86, 36, 0.4);
  background-color: #181818;
}

.label-input input::placeholder {
  color: rgba(255, 255, 255, 0.4);
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