<script setup>
  import EventsVertical from "@/components/EventsVertical.vue";
  import LocationModal from "@/components/LocationModal.vue";
  import {ref, watch} from "vue";
  import {useLocationStore} from "@/stores/LocationStore.js";
  import {useCitiesModalStore} from "@/stores/CitiesModalStore.js";
  import CitiesModal from "@/components/CitiesModal.vue";

  const locationStore = useLocationStore()
  const citiesModalStore = useCitiesModalStore()
  const locationModal = ref(null)
  const showLocationModal = ref(true)

  //page not scrollable when modal open
  watch(
      () => citiesModalStore.visible,
      (isVisible) => {
        document.body.style.overflow = isVisible ? 'hidden' : 'auto'
      }
  )

  watch(
      () => showLocationModal.value,
      (isVisible) => {
        document.body.style.overflow = isVisible ? 'hidden' : 'auto'
      }
  )

  watch(
      () => [locationStore.city, locationStore.latitude, locationStore.longitude], // what to watch
      ([city, latitude, longitude]) => {
        if (city || latitude || longitude) {
          showLocationModal.value = false
        }
      },
      { immediate: true } // optional: runs once on mount if values already set
  )
</script>

<template>
<!--  <CitiesModal v-if="citiesModalStore.visible" class="cities-modal"/>-->
  <div class="container">

    <div v-if="showLocationModal || citiesModalStore.visible" ref="modalBackground" class="modal-background"></div>
    <LocationModal v-if="showLocationModal" ref="locationModal" class="location-modal"/>

    <EventsVertical/>
  </div>
</template>

<style scoped>
.container{
  position: relative;
  padding: 2rem 1rem;
  width: 100%;
}

.modal-background{
  position: fixed;
  left: 0;
  bottom: 0;
  height: 100vh;
  width: 100vw;
  background-color: black;
  opacity: 0.8;
  z-index: 100;
}

.location-modal{
  position: fixed;
  top: calc((100vh - 20vh) / 2);
  left: 50%;
  transform: translate(-50%, -50%);
  visibility: visible;
  z-index: 101;
}
</style>