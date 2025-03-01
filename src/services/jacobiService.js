import axios from 'axios';

class JacobiService {
    async calculateJacobi(A, B) {
        try {
            const response = await axios.post('/api/jacobi', { A, B }, {});
            console.log('Jacobi calculation result:', response.data);
            return response.data; 
        } catch (error) {
            console.error('Error while calculating Jacobi method:', error.response || error);
            throw error; 
        }
    }
}

export default new JacobiService();
