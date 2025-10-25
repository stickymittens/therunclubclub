<script setup>
import axios from "axios";
import { ref } from "vue";

const date = ref('');
const time = ref('10:00');
const distance = ref('');
const pace = ref('');
const meetingPoint = ref('');
const description = ref('');
const message = ref("");

const event = ref({
  distance: Number(distance.value),
  pace: Number(pace.value),
  meetingPoint: meetingPoint.value,
  eventDescription: description.value,
  dateTime: `${date.value}T${time.value}:00+02:00`,
  // club: { id: 1 }  // optional if club is required
});

const addEvent = async () => {
  try {
    const isoString = `${date.value}T${time.value}:00+02:00`; // Spain time
    event.value.dateTime = isoString;

    const response = await axios.post("http://localhost:8080/events", event.value)
    message.value = `"${response.data.distance}"km run added successfully!`;

    // reset form
    date.value = '';
    time.value = '';
    distance.value = '';
    pace.value = '';
    meetingPoint.value = '';
    description.value = '';

    event.value = {
      distance: 0,
      pace: 0,
      meetingPoint: '',
      eventDescription: '',
      dateTime: ''
    };
  } catch (error) {
    console.error(error);
    message.value = "Error adding event.";
  }
};
</script>

<template>
  <div class="container">
    <h2 >Add New Event</h2>

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
        <label>Meeting Point</label>
        <textarea v-model="event.meetingPoint"></textarea>
      </div>

      <div>
        <label>Distance</label>
        <textarea v-model="event.distance"></textarea>
      </div>

      <div>
        <label>Pace</label>
        <textarea v-model="event.pace"></textarea>
      </div>

      <div>
        <label>Description</label>
        <textarea v-model="event.eventDescription"></textarea>
      </div>


      <button type="submit">
        Add Event
      </button>
    </form>

    <p v-if="message">{{ message }}</p>
  </div>
</template>


