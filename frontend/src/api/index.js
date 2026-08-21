import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:8082/api', // Asegúrate de que coincida con tu puerto de Spring Boot
    headers: {
        'Content-Type': 'application/json',
    },
});

export default apiClient;