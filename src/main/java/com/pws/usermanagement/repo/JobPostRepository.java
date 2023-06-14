package com.pws.usermanagement.repo;

import com.pws.usermanagement.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobPostRepository extends JpaRepository<JobPost, UUID> {
}
