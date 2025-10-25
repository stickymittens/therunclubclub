import { createRouter, createWebHistory } from 'vue-router'
import AddClubEvent from '../views/AddClubEvent.vue'
import EventView from "@/views/EventView.vue";
import Events from "@/views/Events.vue";
import Profile from "@/views/Profile.vue";

const routes = [
    {
        path: '/add-event',
        name: 'AddEvent',
        component: AddClubEvent
    },
    {
        path: '/events',
        name: 'Events',
        component: Events
    },
    {
        path: '/profile',
        name: 'Profile',
        component: Profile
    },
    {
        path: '/events/:id',
        name: 'EventView',
        component: EventView
    },
    // Later you can add more routes like profile, home, etc.
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
