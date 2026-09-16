package com.handymanhub.backend.service;

import com.handymanhub.backend.dto.TaskCreateRequest;
import com.handymanhub.backend.dto.TaskDto;
import com.handymanhub.backend.exception.ResourceNotFoundException;
import com.handymanhub.backend.model.Task;
import com.handymanhub.backend.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskDto create(TaskCreateRequest request) {
        Task task = new Task(request.getTitle(), request.getDescription());
        Task saved = taskRepository.save(task);
        return toDto(saved);
    }

    public TaskDto getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
        return toDto(task);
    }

    public List<TaskDto> getAll() {
        return taskRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found: " + id);
        }
        taskRepository.deleteById(id);
    }

    private TaskDto toDto(Task task) {
        return new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.getCreatedAt());
    }
}
