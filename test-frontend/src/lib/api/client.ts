import axios from "axios"
import { Library, SearchCriterion } from "@/lib/search/types.ts"

const API_URL = "http://localhost:8080/api/v1"

export const getLibrariesByQuery = async (query: SearchCriterion[]): Promise<Library[]> => {
  try {
    const queryCopy = JSON.parse(JSON.stringify(query))
    const formattedQuery = processCriteria(queryCopy)
    const response = await axios.post<Library[]>(`${API_URL}/library/query`, formattedQuery, {
      headers: {
        "Content-Type": "application/json",
      },
    })
    return response.data
  } catch (error) {
    console.error("Failed to fetch libraries", error)
    throw error
  }
}

const processCriteria = (rawCriteria: SearchCriterion[]): SearchCriterion[] => {
  const cleaned = cleanCriteria(rawCriteria)
  const filtered = filterValidCriteria(cleaned)
  return formatCriteria(filtered)
}

export const filterCriteria = (criteria: SearchCriterion[]): SearchCriterion[] => {
  const cleaned = cleanCriteria(criteria)
  return filterValidCriteria(cleaned)
}

const cleanCriteria = (criteria: SearchCriterion[]): SearchCriterion[] => {
  return criteria.map((criterion) => {
    const cleanedCriterion = {
      field: criterion.field,
      op: criterion.op,
      subQuery: criterion.subCriteria && criterion.subCriteria.length > 0,
      value: criterion.value,
      subCriteria:
        criterion.subCriteria && criterion.subCriteria.length > 0 ? criterion.subCriteria : undefined,
    }

    if (!criterion.value) {
      cleanedCriterion.value = undefined
    }

    if (criterion.subQuery) {
      cleanedCriterion.subCriteria = cleanCriteria(criterion.subCriteria)
    }

    return cleanedCriterion
  })
}

const filterValidCriteria = (criteria: SearchCriterion[]): SearchCriterion[] => {
  return criteria.filter((criterion) => {
    if (!criterion.field || !criterion.op) {
      return false
    }

    if (criterion.subQuery) {
      if (Array.isArray(criterion.subCriteria) && criterion.subCriteria.length > 0) {
        criterion.subCriteria = filterValidCriteria(criterion.subCriteria)
        return criterion.subCriteria.length > 0
      }
      return false
    }

    return criterion.value !== undefined && criterion.value !== null
  })
}

const formatCriteria = (criteria: SearchCriterion[]): SearchCriterion[] => {
  return criteria.map((criterion) => {
    if (criterion.value !== undefined) {
      criterion.value = criterion.value.toString()
    }

    if (["like", "notLike"].includes(criterion.op?.toLowerCase())) {
      formatLikeCriterion(criterion)
    } else if (["in", "notIn"].includes(criterion.op?.toLowerCase())) {
      formatCollectionCriterion(criterion)
    }

    if (criterion.subQuery) {
      criterion.subCriteria = formatCriteria(criterion.subCriteria)
    }

    return criterion
  })
}

const formatLikeCriterion = (criterion: SearchCriterion): SearchCriterion => {
  if (criterion.value && !criterion.value.includes("%")) {
    criterion.value = `%${criterion.value}%`
  }
  return criterion
}

const formatCollectionCriterion = (criterion: SearchCriterion): SearchCriterion => {
  const regex = /[\p{L}\p{N}\-.]+/gu

  if (criterion.value) {
    const matches = criterion.value.match(regex)
    if (matches && matches.length > 0) {
      criterion.value = `[${matches.join(", ")}]`
    } else {
      criterion.value = "[]"
    }
  }

  return criterion
}
