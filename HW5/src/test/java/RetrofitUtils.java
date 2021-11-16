import io.qameta.allure.okhttp3.AllureOkHttp3;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public class RetrofitUtils {
    static HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new PrettyLogger());
    static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static Retrofit getRetrofit(){
        logging.setLevel(BODY);
        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(new AllureOkHttp3());
        return new Retrofit.Builder()
                .baseUrl(ConfigUtils.getBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }
}
