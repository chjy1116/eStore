package kr.ac.hansung.cse.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kr.ac.hansung.cse.model.Cart;
import kr.ac.hansung.cse.model.CartItem;
import kr.ac.hansung.cse.model.Product;
import kr.ac.hansung.cse.model.User;
import kr.ac.hansung.cse.service.CartItemService;
import kr.ac.hansung.cse.service.CartService;
import kr.ac.hansung.cse.service.ProductService;
import kr.ac.hansung.cse.service.UserService;

@RestController // @Controller + @ResponseBody
@RequestMapping("/rest/cart")
public class CartRestController {

	@Autowired
	private CartService cartService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/{cartId}", method = RequestMethod.GET)
	public ResponseEntity<Cart> getCartById(@PathVariable(value = "cartId") int cartId) {
		Cart cart = cartService.getCartById(cartId);
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}

	@RequestMapping(value = "/add/{productId}", method = RequestMethod.PUT)
	public ResponseEntity<Void> addItem(@PathVariable(value = "productId") int productId, HttpServletRequest request) {

		Logger logger = LoggerFactory.getLogger(CartRestController.class);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		User user = userService.getUserByUsername(username);
		Cart cart = user.getCart();
		Product product = productService.getProductById(productId);

		List<CartItem> cartItems = cart.getCartItems();

		for (int i = 0; i < cartItems.size(); i++) {
			if (product.getId() == cartItems.get(i).getProduct().getId()) {
				CartItem cartItem = cartItems.get(i);
				cartItem.setQuantity(cartItem.getQuantity() + 1);
				cartItem.setTotalPrice(product.getPrice() * cartItem.getQuantity());
				cartItemService.addCartItem(cartItem);

				logger.info("update CartItem set cartId=" + cart.getCartId() + ", productId=" + cartItem.getProduct().getId()+", quantity="+cartItem.getQuantity()+", totalPrice=" + cartItem.getTotalPrice() + " where cartItemId="+cartItem.getCartItemId());
				return new ResponseEntity<Void>(HttpStatus.OK);
			}
		}

		CartItem cartItem = new CartItem();
		cartItem.setProduct(product);
		cartItem.setQuantity(1);
		cartItem.setTotalPrice(product.getPrice() * cartItem.getQuantity());
		cartItem.setCart(cart);

		cartItemService.addCartItem(cartItem);

		cart.getCartItems().add(cartItem);
		product.getCartItemList().add(cartItem);

		logger.info("update CartItem set cartId=" + cart.getCartId() + ", productId=" + cartItem.getProduct().getId()+", quantity="+cartItem.getQuantity()+", totalPrice=" + cartItem.getTotalPrice() + " where cartItemId="+cartItem.getCartItemId());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/cartitem/{productId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> removeItem(@PathVariable(value = "productId") int productId) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String username = authentication.getName();
		User user = userService.getUserByUsername(username);
		Cart cart = user.getCart();
		CartItem cartItem = cartItemService.getCartByProductId(cart.getCartId(), productId);
		cartItemService.removeCartItem(cartItem);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{cartId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> clearCart(@PathVariable(value = "cartId") int cartId) {
		Cart cart = cartService.getCartById(cartId);
		cartItemService.removeAllCartItems(cart);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/pluscartitem/{productId}", method = RequestMethod.PUT)
	public ResponseEntity<Void> plusQuantity(@PathVariable(value = "productId") int productId) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String username = authentication.getName();
		User user = userService.getUserByUsername(username);
		Cart cart = user.getCart();
		CartItem cartItem = cartItemService.getCartByProductId(cart.getCartId(), productId);
		Product product = productService.getProductById(productId);
		if (cartItem.getQuantity() < product.getUnitInStock()) {
			cartItem.setQuantity(cartItem.getQuantity() + 1);
			cartItem.setTotalPrice(product.getPrice() * cartItem.getQuantity());

			cartItemService.editCartItem(cartItem);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/minuscartitem/{productId}", method = RequestMethod.PUT)
	public ResponseEntity<Void> minusQuantity(@PathVariable(value = "productId") int productId) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		User user = userService.getUserByUsername(username);
		Cart cart = user.getCart();
		CartItem cartItem = cartItemService.getCartByProductId(cart.getCartId(), productId);
		
		if (cartItem.getQuantity() > 1) {
			cartItem.setQuantity(cartItem.getQuantity() - 1);
			cartItem.setTotalPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
			cartItemService.editCartItem(cartItem);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else if (cartItem.getQuantity() == 1) {
			cartItemService.removeCartItem(cartItem);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return null;
	}
}
