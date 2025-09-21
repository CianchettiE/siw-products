package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Comment;
import it.uniroma3.siw.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Comment comment) {
        commentRepository.save(comment);
    }
    
    @Transactional
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
