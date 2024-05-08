import axios from 'axios';
import type { components, operations } from 'api-schema';

const API_URL = 'http://localhost:8080/api/v1';

export async function getLibrariesByQuery(query: components['schemas']['SearchCriterion'][]): Promise<components['schemas']['Library'][]> {
    try {
        const response = await axios.post<operations['getLibrariesByQuery']['responses']['200']['content']['*/*']>(
            `${API_URL}/library/query`,
            query,
            {
                headers: {
                    'Content-Type': 'application/json'
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error('Failed to fetch libraries', error);
        throw error;
    }
}
