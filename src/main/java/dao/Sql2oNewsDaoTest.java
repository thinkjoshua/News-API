
package dao;

import models.Department;
import models.News;
import models.User;
import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class Sql2oNewsDaoTest {
    private Sql2oDepartmentDao departmentDao = new Sql2oDepartmentDao();
    private static Sql2oNewsDao newsDao = new Sql2oNewsDao();
    private static Sql2oUserDao userDao = new Sql2oUserDao();

//    @Rule
//    public DatabaseRule databaseRule = new DatabaseRule();

    private News altNews(){
        News news = new News("Attempted murder","Babu Owino shoots a DJ at BClub");
        news.setAuthor(newUser().getName());
        newsDao.add(news);
        return news;
    }
    private News altNews2(){
        News news = new News("Miracle","Joyce Mungai grows a foot taller, thank God");
        news.setType("Religion");
        news.setAuthor(newUser2().getName());
        newsDao.add(news);
        return news;
    }

    private Department newDepartment(){
        Department department = new Department("Forensics","Research and determine cause of murders", 5);
        departmentDao.add(department);
        return department;
    }

    private User newUser(){
        User user = new User("Mick Jenkins","Assistant","GOAT","Rap");
        userDao.add(user);
        return user;
    }

    private User newUser2(){
        User user = new User("Kendrick Lamar","Lead","GOAT","Rap");
        userDao.add(user);
        return user;
    }

    @Test
    public void newsGetsSavedToDb(){
        News news = altNews();
        assertNotEquals(0,news.getId());
    }

    @Test
    public void findNewsById(){
        News news = altNews();
        News news2 = altNews2();
        assertTrue(news.equals(newsDao.findById(news.getId())));
    }


    @Test
    public void getDepartmentAfterCrosscheck_String(){
        Department department = newDepartment();
        News news2 = altNews2();
        assertEquals("Religion",newsDao.findById(news2.getId()).getType());
    }

    @Test
    public void findAllNewsPosts_int(){
        News news = altNews();
        News news2 = altNews2();
        assertEquals(2,newsDao.allNews().size());
    }

    @Test
    public void simpleDeleteNewsById_true(){
        News news = altNews();
        News news2 = altNews2();
        newsDao.deleteById(news.getId());
        assertEquals(1,newsDao.allNews().size());
    }

    @Test
    public void deleteAllNewsPosts(){
        News news = altNews();
        News news2 = altNews2();
        newsDao.deleteAll();
        assertEquals(0,newsDao.allNews().size());
    }

    @Test
    public void addNewsToGeneralDepartment(){
        User user = newUser();
        News news = altNews();
        newsDao.addNewsToDepartment(0,news.getId(),user.getId());
        assertEquals(1,departmentDao.allDepartmentNews(0).size());
        assertEquals("General",departmentDao.allDepartmentNews(0).get(0).getType());
    }

    @Test
    public void addNewsToSpecificDepartment(){
        Department department = newDepartment();
        User user = newUser();
        News news = altNews2();
        newsDao.addNewsToDepartment(department.getId(),news.getId(),user.getId());
        assertEquals(1,departmentDao.allDepartmentNews(department.getId()).size());
    }

