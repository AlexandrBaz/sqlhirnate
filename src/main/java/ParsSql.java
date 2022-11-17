import entity.Course;
import entity.Student;
import entity.Subscription;
import entity.Teacher;
import enums.CourseType;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ParsSql {


    public static void parseSql(String uri) throws IOException, ParseException {
        List<String> lines = Files.readAllLines(Paths.get(uri));
        for (String line : lines) {
            if (line.contains("INSERT INTO `Courses`")) {
                String clearData = line.replaceAll("INSERT INTO `Courses` \\(id, description, duration, name, price, price_per_hour, students_counts, type, teacher_id\\) VALUES", "");
                clearData = clearData.replaceAll(";", "");
//                parseAndSetCourse(clearData);
            }
            if (line.contains("INSERT INTO `PurchaseList`")) {
                String clearData = line.replaceAll("INSERT INTO `PurchaseList` \\(student_name, course_name, price, subscription_date\\) VALUES ", "");
                clearData = clearData.replaceAll(";", "");
            }
            if (line.contains("INSERT INTO `Students`")) {
                String clearData = line.replaceAll("INSERT INTO `Students` \\(id, name, age, registration_date\\) VALUES ", "");
                clearData = clearData.replaceAll(";", "");
                setStudent(clearData);
            }
            if (line.contains("INSERT INTO `Subscriptions`")) {
                String clearData = line.replaceAll("INSERT INTO `Subscriptions` \\(student_id, course_id, subscription_date\\) VALUES ", "");
                clearData = clearData.replaceAll(";", "");
//                setSubscription(clearData);
            }
            if (line.contains("INSERT INTO `Teachers`")) {
                String clearData = line.replaceAll("INSERT INTO `Teachers` \\(id, name, salary, age\\) VALUES ", "");
                clearData = clearData.replaceAll(";", "");
//                setTeacher(clearData);
            }
        }
    }

    private static void setStudent(String clearData) throws ParseException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        String[] fragments = clearData.split("\\),\\(");
        for (String frag : fragments) {
            String data = frag.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("'", "");
            String[] fields = data.split(",");

            session.getTransaction().begin();
            Student student = new Student();
            student.setName(fields[1]);
            student.setAge(Integer.parseInt(fields[2]));
            Date date = format.parse(fields[3]);
            student.setRegistrationDate(date);
            session.save(student);
            session.getTransaction().commit();
        }
        session.close();
        HibernateUtil.close();
    }

    private static void setSubscription(String clearData) throws ParseException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);


        String[] fragments = clearData.split("\\),\\(");
        for (String frag : fragments) {
            String data = frag.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("'", "");
            String[] fields = data.split(",");

            session.getTransaction().begin();
            Subscription subscription = new Subscription();
            subscription.setStudentId(Integer.parseInt(fields[0]));
            subscription.setCoursedId(Integer.parseInt(fields[1]));
            Date date = format.parse(fields[2]);
            subscription.setSubscriptionDate(date);

            session.save(subscription);
            session.getTransaction().commit();

        }
        session.close();
        HibernateUtil.close();
    }

    private static void setTeacher(String clearData) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String[] fragments = clearData.split("\\),\\(");
        for (String frag : fragments) {
            String data = frag.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("'", "");
            String[] fields = data.split(",");

            session.getTransaction().begin();
            Teacher teacher = new Teacher();
            teacher.setName(fields[1]);
            teacher.setSalary(Integer.parseInt(fields[2]));
            teacher.setAge(Integer.parseInt(fields[3]));
            session.save(teacher);
            session.getTransaction().commit();
        }
        session.close();
        HibernateUtil.close();
    }

    private static void parseAndSetCourse(String clearData) {
        String[] fragments = clearData.split("\\),\\(");
        for (String frag : fragments) {
            String data = frag.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("'", "");
            String[] fields = data.split(",");


            Session session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            Course course = new Course();
            course.setName(fields[1]);
            course.setDuration(Integer.parseInt(fields[2]));
            course.setType(CourseType.valueOf(fields[3]));
            course.setDescription(fields[4]);
            course.setTeacherId(Integer.parseInt(fields[5]));
            course.setStudentsCount(Integer.parseInt(fields[6]));
            course.setPrice(Integer.parseInt(fields[7]));
            course.setPricePerHour(Float.parseFloat(fields[8]));
            session.save(course);
            session.getTransaction().commit();
            session.close();
            HibernateUtil.close();
        }
    }
}