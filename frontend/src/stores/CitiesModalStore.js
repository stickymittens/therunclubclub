import { defineStore } from 'pinia'

export const useCitiesModalStore = defineStore('citiesModal', {
    state: () => ({
        visible: false
    }),
    actions: {
        show() {
            this.visible = true
            localStorage.setItem('citiesModalVisible', 'true')
        },
        hide() {
            this.visible = false
            localStorage.setItem('citiesModalVisible', 'false')
        },
        load() {
            const saved = localStorage.getItem('citiesModalVisible')
            if (saved !== null) {
                this.visible = saved === 'true'
            }
        }
    }
})
