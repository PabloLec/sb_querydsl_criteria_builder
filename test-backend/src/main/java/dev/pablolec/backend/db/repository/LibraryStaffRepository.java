package dev.pablolec.backend.db.repository;

import dev.pablolec.backend.db.model.LibraryStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryStaffRepository extends JpaRepository<LibraryStaff, Integer> {}
