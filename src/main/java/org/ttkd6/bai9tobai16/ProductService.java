package org.ttkd6.bai9tobai16;

import org.ttkd6.Product;

import java.util.*;
import java.util.stream.Collectors;

public class ProductService {
    public List<Product> createProductList() {
        List<Product> listProduct = new ArrayList<>();

        // Tạo 10 đối tượng Product
        for (int i = 1; i <= 10; i++) {
            Product product = Product.builder()
                    .id(i)
                    .categoryId(i % 3 + 1)
                    .name("Product " + i)
                    .saleDate(new Date()) // Ngày hiện tại
                    .quality(10 * i)
                    .isDelete(false) // Không bị xóa
                    .build();
            listProduct.add(product);
        }
        return listProduct;
    }

    //Bai11-------------------------------------------------------------------------------

    // Don't use stream
    public String filterProductById(List<Product> listProduct, Integer idProduct){
        for(Product p : listProduct){
            if(p.getId().equals(idProduct)){
                return p.getName();
            }
        }
        return null;
    }

    // Use stream
    public String filterProductByIdC2(List<Product> listProduct, Integer idProduct){
        Optional<Product> product = listProduct.stream()
                .filter(p -> p.getId().equals(idProduct))
                .findFirst();
        return product.map(Product::getName).orElse(null);
    }

    //Bai12--------------------------------------------------------------------------------

    // Don't use stream
    public List<Product> filterProductByQuality(List<Product> products){
        List<Product> filterProducts = new ArrayList<>();
        for(Product p : products){
            if(p.getQuality() > 0 && !p.getIsDelete()){
                filterProducts.add(p);
            }
        }
        return filterProducts;
    }

    // Use stream
    public List<Product> filterProductByQualityC2(List<Product> products){
        List<Product> productList = products.stream()
                .filter(p -> p.getQuality() > 0 && !p.getIsDelete())
                .collect(Collectors.toList());
        return productList;
    }

    //Bai13---------------------------------------------------------------------------------

    public List<Product> filterProductBySaleDate(List<Product> products){
        Date currentDate = new Date();
        List<Product> filterProducts = new ArrayList<>();
        for(Product p : products){
            if(p.getSaleDate().compareTo(currentDate) == 0 && !p.getIsDelete()){
                filterProducts.add(p);
            }
        }
        return filterProducts;
    }

    // Use stream
    public List<Product> filterProductBySaleDateC2(List<Product> products){
        Date currentDate = new Date();
        List<Product> productList = products.stream()
                .filter(p -> p.getSaleDate().compareTo(currentDate) == 0 && !p.getIsDelete())
                .collect(Collectors.toList());
        return productList;
    }

    //Bai14--------------------------------------------------------------------------------------
    public int totalProduct(List<Product> productList){
        int sum = 0;
        for(Product p : productList){
            if(!p.getIsDelete()){
                sum+=p.getQuality();
            }
        }
        return sum;
    }

    // Use reducer
    public int totalProductC2(List<Product> productList){
        int sum;
        List<Product> filterProducts = new ArrayList<>();
        sum = productList.stream()
                .filter(p -> !p.getIsDelete())
                .map(Product::getQuality)
                .reduce(0, Integer::sum);
        return sum;
    }

    //Bai15--------------------------------------------------------------------------------------
    public Boolean isHaveProductInCategory(List<Product> listProduct, Integer categoryId){
        List<Product> filterProducts = new ArrayList<>();
        for(Product p : listProduct){
            if (p.getCategoryId() == categoryId) {
                filterProducts.add(p);
            }
        }
        return true;
    }

    public List<Product> isHaveProductInCategoryC2(List<Product> listProduct, Integer categoryId){
        List<Product> productList = listProduct.stream()
                .filter(p -> p.getCategoryId().equals(categoryId))
                .toList();
        return productList;
    }

    //Bai16--------------------------------------------------------------------------------------
    // Don't use stream
    public List<List<String>> filterProductBySaleDate16(List<Product> listProduct){
        List<List<String>> results = new ArrayList<>();
        Date cDate = new Date();

        for(Product p: listProduct){
            if(p.getSaleDate().after(cDate) && p.getQuality() > 0){
                List<String> product = new ArrayList<>();
                product.add(p.getName());
                product.add(p.getId().toString());
                results.add(product);
            }
        }
        return results;
    }

    // use stream
    public List<List<String>> filterProductBySaleDate16C2(List<Product> listProduct){
        List<List<String>> results = new ArrayList<>();
        Date cDate = new Date();

        List<List<String>> proListList = listProduct.stream()
                .filter(p -> p.getSaleDate().after(cDate) && p.getQuality() > 0)
                .map(p -> {
                    List<String> product = new ArrayList<>();
                    product.add(p.getName());
                    product.add(p.getId().toString());
                    return product;
                })
                .toList();
        return results;
    }

    public static void main(String[] args) {
        ProductService productService = new ProductService();
        List<Product> products = productService.createProductList();

        // In danh sách sản phẩm
        //products.forEach(System.out::println);
        products.forEach(p -> System.out.println(p));

        //System.out.println(productService.filterProductById(products, 1));;
        //System.out.println(productService.filterProductByIdC2(products, 2));

        //System.out.println(productService.filterProductBySaleDate16C2(products));

        //System.out.println(productService.totalProduct(products));

        System.out.println(productService.totalProductC2(products));
    }
}
