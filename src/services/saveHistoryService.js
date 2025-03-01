import axios from 'axios';

class SaveHistoryService {
    async saveHistory(history) {
        console.log("Запит на збереження історії...");
        try {
            const response = await axios.post('/api/jacobi/histories', history, {
                headers: {
                    'Content-Type': 'application/json'}
            });
            if (response.status === 302) {
                console.log('Redirect URL:', response.headers['location']);
              } else {
                console.log('Jacobi calculated:', response.data);
              }
            console.log('History saved:', response.data);
            return response.data;
        } catch (error) {
            console.error('Error while registering history:', error.response || error); 
            throw new Error('Failed to save history. Please try again later.');
        }
    }
}

export default new SaveHistoryService();
