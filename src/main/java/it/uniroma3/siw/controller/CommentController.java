package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Comment;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Product;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CommentService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ProductService;
import jakarta.validation.Valid;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CredentialsService credentialsService;

    @GetMapping("/user/formNewComment/{productId}")
    public String formNewComment(@PathVariable("productId") Long productId, Model model) {
        Product product = this.productService.findById(productId);
        Comment comment = new Comment();
        comment.setProduct(product);
        model.addAttribute("comment", comment);
        return "user/formNewComment.html";
    }

    @PostMapping("/user/newComment/{productId}")
    public String newComment(@PathVariable("productId") Long productId, @Valid @ModelAttribute("comment") Comment comment, BindingResult bindingResult, Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        User author = credentials.getUser();
        
        Product product = this.productService.findById(productId);
        comment.setAuthor(author);
        comment.setProduct(product);

        if (!bindingResult.hasErrors()) {
            this.commentService.save(comment);
            return "redirect:/products/" + product.getId();
        } else {
            model.addAttribute("comment", comment);
            return "user/formNewComment.html";
        }
    }
    
    @GetMapping("/user/formUpdateComment/{commentId}")
    public String formUpdateComment(@PathVariable("commentId") Long commentId, Model model) {
        Comment comment = this.commentService.findById(commentId);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        if(comment.getAuthor().equals(credentials.getUser())) {
            model.addAttribute("comment", comment);
            return "user/formUpdateComment.html";
        }
        return "redirect:/products/" + comment.getProduct().getId();
    }
    
    @PostMapping("/user/updateComment/{commentId}")
    public String updateComment(@PathVariable("commentId") Long commentId, @Valid @ModelAttribute("comment") Comment updatedComment, BindingResult bindingResult, Model model) {
        Comment originalComment = this.commentService.findById(commentId);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        
        if(!originalComment.getAuthor().equals(credentials.getUser())) {
            return "redirect:/products/" + originalComment.getProduct().getId();
        }
        
        if (!bindingResult.hasErrors()) {
            originalComment.setTitle(updatedComment.getTitle());
            originalComment.setText(updatedComment.getText());
            originalComment.setRating(updatedComment.getRating());
            this.commentService.save(originalComment);
            return "redirect:/products/" + originalComment.getProduct().getId();
        } else {
            model.addAttribute("comment", originalComment);
            return "user/formUpdateComment.html";
        }
    }

    @GetMapping("/user/deleteComment/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId) {
        Comment comment = this.commentService.findById(commentId);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        
        if(comment.getAuthor().equals(credentials.getUser())) {
            Long productId = comment.getProduct().getId();
            this.commentService.deleteById(commentId);
            return "redirect:/products/" + productId;
        }
        return "redirect:/products/" + comment.getProduct().getId();
    }
}
