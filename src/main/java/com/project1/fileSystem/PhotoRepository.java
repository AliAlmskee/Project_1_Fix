package com.project1.fileSystem;

import com.project1.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo,Long> {
}
