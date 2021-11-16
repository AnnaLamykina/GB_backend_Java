import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class Test_Product {
    static ProductService productService;
    boolean skipClean = false;
    Product product;
    Faker faker = new Faker();

    int id;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));
    }

    @Test
    @SneakyThrows
    @DisplayName("create product in category FOOD")
    void createProductInFoodCategoryTest() {
        Response<Product> response = productService.createProduct(product).execute();
        id = response.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }

    @Test
    @SneakyThrows
    void checkCreationUniqueTest() {
        createProductInFoodCategoryTest();
        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }

    @Test
    @SneakyThrows
    void checkNonNegativePriceTest() {
        product = product.withPrice((int) (Math.random() * -10000));
        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }

    @Test
    @SneakyThrows
    void checkCreationProductWithNotExistCategoryTest() {
        skipClean=true;
        product = product.withCategoryTitle("NotExistCategory");
        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }

    @Test
    @SneakyThrows
    @DisplayName("create product with ID")
    void createProductWithIDTest() {
        skipClean=true;
        product=product.withId(10);
        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }

    @Test
    @SneakyThrows
    void postProductTest()  {
        Response<Product> response = productService.createProduct(product).execute();
        id = response.body().getId();
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
    }

    @Test
    @SneakyThrows
    void deleteDeletedProductTest() {
        skipClean=true;
        Response<Product> response = productService.createProduct(product).execute();
        id = response.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        Response<ResponseBody> response1 = productService.deleteProduct(id).execute();
        assertThat(response1.isSuccessful(), CoreMatchers.is(true));
        Response<ResponseBody> response2 = productService.deleteProduct(id).execute();
        assertThat(response2.isSuccessful(), CoreMatchers.is(false));
    }

    @Test
    @SneakyThrows
    void updateCreatedTest(){
        createProductInFoodCategoryTest();
        product=product.withId(id);
        product=product.withTitle("NewSuperName");
        Response<Product> response = productService.updateProduct(product).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }

    @Test
    @SneakyThrows
    void updateNotExistTest(){
        createProductInFoodCategoryTest();
        tearDown();
        skipClean=true;
        product=product.withId(id);
        product=product.withTitle("NewSuperName");
        Response<Product> response = productService.updateProduct(product).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }

    @Test
    @SneakyThrows
    void getCreatedTest(){
        createProductInFoodCategoryTest();
        Response<Product> response = productService.getProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getTitle(),CoreMatchers.is(product.getTitle()));
    }

    @Test
    @SneakyThrows
    void getNotExistTest(){
        createProductInFoodCategoryTest();
        tearDown();
        skipClean=true;
        Response<Product> response = productService.getProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        if (skipClean){
            return;
        }
        Response<ResponseBody> response = productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }

}

