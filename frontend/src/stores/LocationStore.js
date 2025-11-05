import { defineStore } from 'pinia'

export const useLocationStore = defineStore('location', {
    state: () => ({
        latitude: null,
        longitude: null,
        city: null
        // city: "BARCELONA"
    }),
    actions: {
        setLocation(lat, lon) {
            this.latitude = lat
            this.longitude = lon
            localStorage.setItem('location', JSON.stringify({ lat, lon }))
        },
        loadLocation() {
            const saved = localStorage.getItem('location')
            if (saved) {
                const { lat, lon } = JSON.parse(saved)
                this.latitude = lat
                this.longitude = lon
            }
        }
    }
})
