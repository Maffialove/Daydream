package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Category;
import org.csu.mypetstore.domain.Item;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/catalog/")
public class CatalogController {

    //商品展示模块，包含Category，Product，Item三种级别的展示
    //包含搜索功能，通过Product name搜索

    @Autowired
    private CatalogService catalogService;

    @GetMapping("view")
    public String view() {
        return "catalog/main";
    }

    @GetMapping("viewCategory")
    public String viewCategory(String categoryId, Model model) {
        if (categoryId != null) {
            List<Product> productList = catalogService.getProductListByCategory(categoryId);
            Category category = catalogService.getCategory(categoryId);
            model.addAttribute("productList", productList);
            model.addAttribute("category", category);
        }
        return "catalog/category";
    }

    @GetMapping("viewProduct")
    public String viewProduct(String productId, Model model) {
        if (productId != null) {
            List<Item> itemList = catalogService.getItemListByProduct(productId);
            Product product = catalogService.getProduct(productId);
            model.addAttribute("product", product);
            model.addAttribute("itemList", itemList);
        }
        return "catalog/product";
    }

    @GetMapping("viewItem")
    public String viewItem(String itemId, Model model) {
        Item item = catalogService.getItem(itemId);
        Product product = item.getProduct();
        catalogService.processProductDescription(product);
        model.addAttribute("item", item);
        model.addAttribute("product", product);
        return "catalog/item";
    }

    @PostMapping("searchProducts")
    public String searchProducts(String keyword, Model model) {
        if (keyword == null || keyword.length() < 1) {
            String msg = "Please enter a keyword to search for, then press the search button.";
            model.addAttribute("msg", msg);
            return "common/error";
        } else {
            List<Product> productList = catalogService.searchProductList(keyword.toLowerCase());
            catalogService.processProductDescription(productList);
            model.addAttribute("productList", productList);
            return "catalog/search";
        }

    }




}
