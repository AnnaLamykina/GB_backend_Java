import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class Test_Category {
    static CategoryService categoryService;

    @BeforeAll
    static void beforeAll() {
        categoryService = RetrofitUtils.getRetrofit().create(CategoryService.class);
    }

    @SneakyThrows
    @Test
    void getCategoryByIdPositiveTest() {
        Response<GetCategoryResponse> response = categoryService.getCategory(1).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }

    @SneakyThrows
    @Test
    void getCategoryWithResponseAssertionsPositiveTest() {
        Response<GetCategoryResponse> response = categoryService.getCategory(1).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(1));
        assertThat(response.body().getTitle(), equalTo("Food"));
        response.body().getProducts().forEach(product ->
                assertThat(product.getCategoryTitle(), equalTo("Food")));
    }

    @SneakyThrows
    @Test
    void testThatFoodPriceAllMoreThanZero() {
        Response<GetCategoryResponse> response = categoryService.getCategory(1).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(1));
        assertThat(response.body().getTitle(), equalTo("Food"));
        response.body().getProducts().forEach(product ->
                assertThat(product.getPrice(), greaterThan(0)));
    }

    @SneakyThrows
    @Test
    void testThatElectronicPriceAllMoreThanZero() {
        Response<GetCategoryResponse> response = categoryService.getCategory(2).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    assertThat(response.body().getId(), equalTo(2));
        assertThat(response.body().getTitle(), equalTo("Electronic"));
        response.body().getProducts().forEach(product ->
                assertThat(product.getPrice(), greaterThan(0)));
    }

    @SneakyThrows
    @Test
    void testThatFurniturePriceAllMoreThanZero() {
        Response<GetCategoryResponse> response = categoryService.getCategory(3).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
     assertThat(response.body().getId(), equalTo(3));
        assertThat(response.body().getTitle(), equalTo("Furniture"));
        response.body().getProducts().forEach(product ->
                assertThat(product.getPrice(), greaterThan(0)));
    }
}
