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
    return criteria.filter(criterion => {
        return criterion.field && criterion.op &&
            (criterion.subQuery !== true || (criterion.subCriteria && criterion.subCriteria.length > 0));
    }).map(criterion => {
        const formattedCriterion: SearchCriterion = {
            field: criterion.field,
            op: criterion.op,
            subQuery: criterion.subQuery,
            value: criterion.value?.toString()
        };

        if (criterion.op.toLowerCase().includes('like')) {
            formatLikeCriteria(formattedCriterion);
        }

        if (criterion.subCriteria && criterion.subCriteria.length > 0) {
            formattedCriterion.subCriteria = formatCriteria(criterion.subCriteria);
        } else {
            formattedCriterion.subCriteria = undefined;
        }

        return formattedCriterion;
    });
}

function formatLikeCriteria(criteria: SearchCriterion): SearchCriterion {
    if (criteria.value && !criteria.value.includes('%')) {
        criteria.value = `%${criteria.value}%`;
    }
    return criteria;
}
