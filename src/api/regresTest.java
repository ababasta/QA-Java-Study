package api;

//import org.testng.annotations.Test;
//import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class regresTest {
    private static final String URL = "https://reqres.in/";

    @Test
    public void checkAvatarAndIdTest(){

        Specifications.initSpecifications(Specifications.requestSpec(URL), Specifications.responseSpec(200));

        List<userData> users = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", userData.class);
        //изменим один ID так, чтобы было хотя бы одно несовпадение
        //if(!users.isEmpty()){
        //    userData firstUser = users.get(0);  // Получаем объект по ссылке

            // Изменяем id у объекта
        //    firstUser.setId(100500);
        //}

        //users.forEach(x-> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));
        //итог: если есть хотя бы одно несовпадение, то тест провален

        //изменим один аватар так, чтобы было вхождение id, но не совпадение
        if(!users.isEmpty()){
            userData firstUser = users.get(0);  // Получаем объект по ссылке

            // Изменяем аватар у объекта
            //firstUser.setAvatar("https://reqres.in/img/faces/70-image.jpg");
        }

        //users.forEach(x-> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));
        users.forEach(x-> Assert.assertTrue(x.getAvatar().contains("/" + x.getId().toString() + "-image.jpg")));
        //итог: тест успешный, но по сути должен быть фейл. Нужна другая проверка

        //тоже самое с перебором элементов списка
        List<String> avatars = users.stream().map(userData::getAvatar).collect(Collectors.toList());
        List<String> ids = users.stream().map(x-> x.getId().toString()).collect(Collectors.toList());
        for(int i=0; i<avatars.size(); i++){
            Assert.assertTrue(avatars.get(i).contains("/" + ids.get(i) + "-image.jpg"));
        }
    }

    @Test
    public void positiveRegistration(){

        Specifications.initSpecifications(Specifications.requestSpec(URL), Specifications.responseSpec(200));

        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        SendRegisterData user = new SendRegisterData("eve.holt@reqres.in", "pistol");
        //ошибка сериализации, если не добавить геттеры в класс

        RegisterData regResponse = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(RegisterData.class);
        //Assert.assertNotNull(RegResponse);
        System.out.println("regResponse.error = " + regResponse.getError());

        Assert.assertEquals(id, regResponse.getId());
        Assert.assertEquals(token, regResponse.getToken());

    }

    @Test
    public void negativeRegistration(){

        Specifications.initSpecifications(Specifications.requestSpec(URL), Specifications.responseSpec(400));

        SendRegisterData user = new SendRegisterData("sydney@fife", "");
        //ошибка сериализации, если не добавить геттеры в класс

        RegisterData regResponse = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(RegisterData.class);
        //Assert.assertNotNull(RegResponse);
        System.out.println("regResponse.error = " + regResponse.getError());

        Assert.assertEquals("Missing password", regResponse.getError());
    }

    @Test
    public void checkYearsSortTest() {

        Specifications.initSpecifications(Specifications.requestSpec(URL), Specifications.responseSpec(200));

        List<UnknownData> unknownData = given()
                .when()
                .get("api/unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UnknownData.class);
        List<Integer> years = unknownData.stream().map(UnknownData::getYear).collect(Collectors.toList());
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(years, sortedYears);
        System.out.println(years);
        System.out.println(sortedYears);
    }

    @Test
    public void checkUserDelete(){

        Specifications.initSpecifications(Specifications.requestSpec(URL), Specifications.responseSpec(204));

        given()
                .when()
                .delete("api/users/2")
                .then().log().all();
    }

    @Test
    public void checkSameTime(){

        Specifications.initSpecifications(Specifications.requestSpec(URL), Specifications.responseSpec(200));

        SendTimeTestData user = new SendTimeTestData("morpheus", "zion resident");
        TimeTestData response = given()
                .body(user)
                .when()
                .put("api/user/2")
                .then().log().all()
                .extract().as(TimeTestData.class);

        // Преобразование строки в ZonedDateTime (с учетом временной зоны)
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(response.getUpdatedAt());

        // Создание форматтера для нужного формата (с точностью до секунд)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        // Форматирование ZonedDateTime с помощью форматтера
        String formattedTime = zonedDateTime.format(formatter);

        // Вывод отформатированной строки
        System.out.println("zonedDateTime: " + formattedTime);

        // Получаем текущее время с точностью до миллисекунд
        Instant instant = Clock.systemUTC().instant();

        // Создаем форматтер для нужного формата (с точностью до секунд)
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                .withZone(Clock.systemUTC().getZone());

        // Форматируем Instant в строку с точностью до секунд
        String formattedCurrentTime = formatter2.format(instant);

        // Преобразуем обратно в Instant, убирая миллисекунды
        //Instant truncatedInstant = Instant.parse(formattedTime);

        System.out.println("formattedCurrentTime: " + formattedCurrentTime);

        Assert.assertEquals(formattedCurrentTime, formattedTime);

    }
}
