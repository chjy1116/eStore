package kr.ac.hansung.cse.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import kr.ac.hansung.cse.model.Product;
import kr.ac.hansung.cse.service.ProductService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private ProductService productService;
	
	@RequestMapping
	public String adminPage() {
		return "admin";
	}
	
	@RequestMapping("/productInventory")
	public String getProducts(Model model) {
		List<Product> products = productService.getProducts();
		model.addAttribute("products", products);
		
		return "productInventory";
	}
	
	@RequestMapping("/productInventory/addProduct")
	public String addProducts(Model model) {
		Product product = new Product();
		
		model.addAttribute("product", product);
		return "addProduct";
	}
	
	@RequestMapping(value="/productInventory/addProduct", method=RequestMethod.POST) //post방법 설정
	public String addProductsPost(@Valid Product product, BindingResult result, HttpServletRequest request) {
		
		if(result.hasErrors()) {
			System.out.println("===Form data has some errors===");
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error:errors) {
				System.out.println(error.getDefaultMessage());
			}
			return "addProduct";
 		}
		
		MultipartFile productImage = product.getProductImage();
		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
		Path savePath = Paths.get(rootDirectory + "//resources//images//" + productImage.getOriginalFilename());
		if(productImage != null && !productImage.isEmpty()) {
			try {
				productImage.transferTo(new File(savePath.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		product.setImageFilename(productImage.getOriginalFilename());
		
		productService.addProduct(product);

		return "redirect:/admin/productInventory";
	}
	
	@RequestMapping("/productInventory/deleteProduct/{id}")
	public String deleteProduct(@PathVariable int id, HttpServletRequest request) {
		
		Product product = productService.getProductById(id);
		
		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
		Path path = Paths.get(rootDirectory + "//resources//images//" + product.getImageFilename());
		
		if(Files.exists(path)) {
			try {
				Files.delete(path);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		productService.deleteProduct(product);
		return "redirect:/admin/productInventory";
	}
	
	@RequestMapping("/productInventory/editProduct/{id}")
	public String editProduct(@PathVariable int id, Model model) {
		
		Product product = productService.getProductById(id);
		
		model.addAttribute("product", product);
		
		return "editProduct";
	}
	
	@RequestMapping(value = "/productInventory/editProduct", method=RequestMethod.POST)
	public String editProductPost(@Valid Product product, BindingResult result, HttpServletRequest request) {
		
		if(result.hasErrors()) {
			System.out.println("===Form data has some errors===");
			List<ObjectError> errors = result.getAllErrors();
			for(ObjectError error:errors) {
				System.out.println(error.getDefaultMessage());
			}
			return "editProduct";
 		}
		
		MultipartFile productImage = product.getProductImage();
		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
		Path savePath = Paths.get(rootDirectory + "//resources//images//" + productImage.getOriginalFilename());
		if(productImage != null && !productImage.isEmpty()) {
			try {
				productImage.transferTo(new File(savePath.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		product.setImageFilename(productImage.getOriginalFilename());
		
		productService.editProduct(product);
		
		return "redirect:/admin/productInventory";
	}
}
