import axios from 'axios';
import { SearchCriterion, Library } from '@/lib/search/types.ts';

const API_URL = 'http://localhost:8080/api/v1';

export async function getLibrariesByQuery(query: SearchCriterion[]): Promise<Library[]> {
    try {
        const formattedQuery = formatCriteria(query);
        const response = await axios.post<Library[]>(
            `${API_URL}/library/query`,
            formattedQuery,
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

function formatCriteria(criteria: SearchCriterion[]): SearchCriterion[] {
    return criteria.map(criterion => {
        const formattedCriterion: SearchCriterion = {
            field: criterion.field,
            op: criterion.op,
            subQuery: criterion.subQuery,
            value: criterion.value?.toString()
        };

        if (criterion.op?.toLowerCase().includes('like')) {
            formatLikeCriteria(formattedCriterion);
        }

        if (!criterion.value) {
            formattedCriterion.value = undefined;
        }

        if (criterion.subCriteria && criterion.subCriteria.length > 0) {
            formattedCriterion.subQuery = true;
            formattedCriterion.subCriteria = formatCriteria(criterion.subCriteria);
        } else {
            formattedCriterion.subCriteria = undefined;
        }

        return formattedCriterion;
    }).filter(criterion => {
        if (!criterion.field || !criterion.op) {
            return false;
        }

        if (criterion.subQuery) {
            return Array.isArray(criterion.subCriteria) && criterion.subCriteria.length > 0;
        }

        return criterion.value !== undefined && criterion.value !== null;
    });
}

function formatLikeCriteria(criteria: SearchCriterion): SearchCriterion {
    if (criteria.value && !criteria.value.includes('%')) {
        criteria.value = `%${criteria.value}%`;
    }
    return criteria;
}
