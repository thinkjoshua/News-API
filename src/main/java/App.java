import com.google.gson.Gson;
import com.google.gson.Gson;
import dao.Sql2oDepartmentDao;
import dao.Sql2oNewsDao;
import dao.Sql2oSitemapDao;
import dao.Sql2oUserDao;
import exceptions.ApiException;
import models.Department;
import models.News;
import models.User;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) {

        Sql2oDepartmentDao departmentDao = new Sql2oDepartmentDao();
        Sql2oUserDao userDao = new Sql2oUserDao();
        Sql2oNewsDao newsDao = new Sql2oNewsDao();
        Sql2oSitemapDao sitemapDao = new Sql2oSitemapDao();
        Gson gson = new Gson();


//        ProcessBuilder process = new ProcessBuilder();
//        int port;
//
//        if (process.environment().get("PORT") != null) {
//            port = Integer.parseInt(process.environment().get("PORT"));
//        } else {
//            port = 4567;
//        }
//        port(port);

        get("/departments", "application/json", (request, response) -> gson.toJson(departmentDao.allDepartments()));

        post("/departments/new", "application/json", (request, response) -> {
            Department department = gson.fromJson(request.body(), Department.class);
            departmentDao.add(department);
            response.status(201);
            return gson.toJson(department);
        });

        get("/departments/:departmentId/details", "application/json", (request, response) -> {
            int departmentId = Integer.parseInt(request.params("departmentId"));
            return gson.toJson(departmentDao.findById(departmentId));
        });

        post("/departments/:departmentId/users/new", "application/json", (request, response) -> {
            int departmentId = Integer.parseInt(request.params("departmentId"));
            Department department = departmentDao.findById(departmentId);

            if (department != null) {
                User employee = gson.fromJson(request.body(), User.class);
                employee.setDepartment(department.getName());
                userDao.add(employee);
                departmentDao.addUserToDepartment(department, employee);
                response.status(201);
                return gson.toJson(employee);
            } else {
                throw new ApiException(404, "Department not found");
            }
        });

        get("/departments/:departmentId/users", "application/json", (request, response) -> {
            int departmentId = Integer.parseInt(request.params("departmentId"));
            return gson.toJson(departmentDao.allDepartmentEmployees(departmentId));
        });

        get("/departments/:departmentId/users/:userId/details", "application/json", (request, response) -> {
            int userId = Integer.parseInt(request.params("userId"));
            User foundUser = userDao.findById(userId);

            if (foundUser != null) {
                return gson.toJson(foundUser);
            } else {
                return "{\"Error 404!\":\"User not found\"}";
            }
        });

        get("/departments/:departmentId/users/:userId/news", "application/json", (request, response) -> {
            int userId = Integer.parseInt(request.params("userId"));
            User foundUser = userDao.findById(userId);

            if (foundUser != null) {
                return gson.toJson(userDao.myNews(userId));
            } else {
                return "{\"Error 404!\":\"User not found\"}";
            }
        });

        post("/departments/:departmentId/users/:userId/news/new", "application/json", (request, response) -> {
            int userId = Integer.parseInt(request.params("userId"));
            int departmentId = Integer.parseInt(request.params("departmentId"));
            User foundUser = userDao.findById(userId);
            Department foundDepartment = departmentDao.findById(departmentId);

            if (foundUser != null && foundDepartment != null) {
                News news = gson.fromJson(request.body(), News.class);
                news.setType(foundDepartment.getName());
                news.setAuthor(foundUser.getName());
                newsDao.add(news);
                newsDao.addNewsToDepartment(departmentId, news.getId(), userId);
                response.status(201);
                return gson.toJson(news);
            } else {
                return "{\"Error 404!\":\"User or Department not found\"}";
            }
        });

        get("/departments/:departmentId/news", "application/json", (request, response) -> {
            int departmentId = Integer.parseInt(request.params("deptId"));
            return gson.toJson(departmentDao.allDepartmentNews(departmentId));
        });


        get("/users", "application/json", (request, response) -> gson.toJson(userDao.allUsers()));

        get("/users/:userId/details", "application/json", (request, response) -> {
            int userId = Integer.parseInt(request.params("userId"));
            User foundUser = userDao.findById(userId);
            if (foundUser != null) {
                return gson.toJson(userDao.findById(userId));
            } else {
                return "{\"Error 404!\":\"User not found.\"}";
            }
        });

        get("/users/:userId/news", "application/json", (request, response) -> {
            int userId = Integer.parseInt(request.params("userId"));
            return gson.toJson(userDao.myNews(userId));
        });

        post("/users/:userId/news/new", "application/json", (request, response) -> {
            int userId = Integer.parseInt(request.params("userId"));
            User foundUser = userDao.findById(userId);

            if (foundUser != null) {
                News news = gson.fromJson(request.body(), News.class);
                news.setAuthor(foundUser.getName());
                newsDao.add(news);
                newsDao.addNewsToDepartment(0, news.getId(), userId);
                response.status(201);
                return gson.toJson(news);
            } else {
                return "{\"Error 404!\":\"User not found. News cannot be posted without an actual user as the author\"}";
            }
        });


        get("/news", "application/json", (request, response) -> gson.toJson(newsDao.allNews()));
        get("/news/general", "application/json", (request, response) -> gson.toJson(newsDao.allGeneralNews()));
        get("/news/departments", "application/json", (request, response) -> gson.toJson(newsDao.allDepartmentNews()));
        get("/news/:newsId/details", "application/json", (request, response) -> {
            int newsId = Integer.parseInt(request.params("newsId"));
            return gson.toJson(newsDao.findById(newsId));
        });


        get("/sitemap", "application/json", (request, response) -> {
            return gson.toJson(sitemapDao.allPaths());
        });

        //FILTERS
        after((req, res) -> res.type("application/json"));
    }
}