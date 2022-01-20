package dao;

import models.Department;
import models.User;
import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("unused")
public class Sql2oDepartmentDaoTest {
    private Sql2oDepartmentDao departmentDao = new Sql2oDepartmentDao();
    private static Sql2oUserDao userDao = new Sql2oUserDao();

//    @Rule
//    public DatabaseRule databaseRule = new DatabaseRule();

    private Department newDepartment(){
        Department department = new Department("Forensics","Research and determine cause of murders", 5);
        departmentDao.add(department);
        return department;
    }

    private Department newDepartment2(){
        Department department = new Department("Crime","Conduct criminal investigations", 5);
        departmentDao.add(department);
        return department;
    }

    private User newUser(){
        User user = new User("Lil Holmes","Lead","Detective","Crime");
        userDao.add(user);
        return user;
    }

    private User newUser2(){
        User user = new User("Dr. Whoever","Lead","Scientist","Forensics");
        userDao.add(user);
        return user;
    }

    @Test
    public void departmentAddedToDatabase(){
        Department department = newDepartment();
        System.out.println(department.getId());
        assertNotEquals(0,department.getId());
    }

    @Test
    public void getDepartmentUsingId(){
        Department department = newDepartment();
        Department department2 = newDepartment2();
        assertTrue(departmentDao.allDepartments().contains(department));
        assertTrue(departmentDao.allDepartments().contains(department2));
    }

    @Test
    public void addUserToDepartment(){
        Department department = newDepartment();
        User user = newUser();
        departmentDao.addUserToDepartment(department,user);
        assertEquals("Forensics",user.getDepartment());
    }

    @Test
    public void getUsersInADepartment(){
        Department department = newDepartment();
        User user = newUser();
        User user2 = newUser2();
        departmentDao.addUserToDepartment(department,user);
        departmentDao.addUserToDepartment(department,user2);
        int posOneId = departmentDao.allDepartmentEmployees(department.getId()).get(0).getId();
        assertEquals(user.getId(),posOneId);
    }

    @Test
    public void deletingEmployeeById(){
        Department department = newDepartment();
        User user = newUser();
        User user2 = newUser2();
        departmentDao.addUserToDepartment(department,user);
        departmentDao.addUserToDepartment(department,user2);

        departmentDao.deleteEmployeeFromDepartment(department,user);
        assertEquals(1,department.getTotalEmployees());
        assertEquals("None",user.getDepartment());
    }

    @Test
    public void deletingAllDepartments(){
        Department department = newDepartment();
        Department department2= newDepartment2();
        departmentDao.deleteAll();
        assertEquals(5,departmentDao.allDepartments().size());
    }

    @Test
    public void deletingDepartmentById(){
        Department department = newDepartment();
        Department department2= newDepartment2();
        departmentDao.deleteDepartmentById(department.getId());
        assertEquals(5,departmentDao.allDepartments().size());
    }



}
