package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Product;
import it.uniroma3.siw.service.ProductService;
import jakarta.validation.Valid;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String getProducts(Model model) {
        model.addAttribute("products", this.productService.findAll());
        return "products.html";
    }

    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", this.productService.findById(id));
        return "product.html";
    }

    @GetMapping("/formSearchProducts")
    public String formSearchProducts() {
        return "formSearchProducts.html";
    }

    @PostMapping("/searchProducts")
    public String searchProducts(Model model, @RequestParam String type) {
        model.addAttribute("products", this.productService.findByType(type));
        return "foundProducts.html";
    }

    // ADMIN ROUTES

    @GetMapping("/admin/formNewProduct")
    public String formNewProduct(Model model) {
        model.addAttribute("product", new Product());
        return "admin/formNewProduct.html";
    }

    @PostMapping("/admin/products")
    public String newProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            this.productService.save(product);
            model.addAttribute("product", product);
            return "redirect:/products/" + product.getId();
        } else {
            return "admin/formNewProduct.html";
        }
    }

    @GetMapping("/admin/manageProducts")
    public String manageProducts(Model model) {
        model.addAttribute("products", this.productService.findAll());
        return "admin/manageProducts.html";
    }

    @GetMapping("/admin/formUpdateProduct/{id}")
    public String formUpdateProduct(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", this.productService.findById(id));
        return "admin/formUpdateProduct.html";
    }
    
    @PostMapping("/admin/updateProduct/{id}")
    public String updateProduct(@PathVariable("id") Long id, @Valid @ModelAttribute("product") Product product, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            Product oldProduct = this.productService.findById(id);
            oldProduct.setName(product.getName());
            oldProduct.setPrice(product.getPrice());
            oldProduct.setDescription(product.getDescription());
            oldProduct.setType(product.getType());
            this.productService.save(oldProduct);
            return "redirect:/products/" + id;
        } else {
            return "admin/formUpdateProduct.html";
        }
    }
    
    @GetMapping("/admin/deleteProduct/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        this.productService.deleteById(id);
        return "redirect:/admin/manageProducts";
    }

    @GetMapping("/admin/setSimilarProduct/{productId}/{similarProductId}")
    public String setSimilarProduct(@PathVariable("productId") Long productId, @PathVariable("similarProductId") Long similarProductId, Model model) {
        Product product = this.productService.findById(productId);
        Product similarProduct = this.productService.findById(similarProductId);
        product.getSimilarProducts().add(similarProduct);
        this.productService.save(product);
        
        model.addAttribute("product", product);
        model.addAttribute("productsToAdd", this.productService.findProductsNotInSimilar(product));
        return "admin/productsToAdd.html";
    }

    @GetMapping("/admin/addSimilarProducts/{id}")
    public String addSimilarProducts(@PathVariable("id") Long id, Model model) {
        Product product = this.productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("productsToAdd", this.productService.findProductsNotInSimilar(product));
        return "admin/productsToAdd.html";
    }
}
