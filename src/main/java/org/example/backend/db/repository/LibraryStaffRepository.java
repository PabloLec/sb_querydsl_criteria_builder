package org.example.backend.db.repository;

import org.example.backend.db.model.LibraryStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryStaffRepository extends JpaRepository<LibraryStaff, Integer> {
}