import entity.Course;
import entity.Student;
import entity.Subscription;
import entity.Teacher;
import enums.CourseType;
import keys.SubscriptionKey;
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
    static Session session = HibernateUtil.getSessionFactory().openSession();

        public static void parseSql(String uri) throws IOException, ParseException {

        List<String> lines = Files.readAllLines(Paths.get(uri));
        for (String line : lines) {
            if (line.contains("INSERT INTO `Courses`")) {
                String clearData = line.replaceAll("INSERT INTO `Courses` VALUES ", "");
                clearData = clearData.replaceAll(";", "");
                parseAndSetCourse(clearData);
            }
            if (line.contains("INSERT INTO `PurchaseList`")) {
                String clearData = line.replaceAll("INSERT INTO `PurchaseList` VALUES ", "");
                clearData = clearData.replaceAll(";", "");
            }
            if (line.contains("INSERT INTO `Students`")) {
                String clearData = line.replaceAll("INSERT INTO `Students` VALUES ", "");
                clearData = clearData.replaceAll(";", "");
                setStudent(clearData);
            }
            if (line.contains("INSERT INTO `Subscriptions`")) {
                String clearData = line.replaceAll("INSERT INTO `Subscriptions` VALUES ", "");
                clearData = clearData.replaceAll(";", "");
                setSubscription(clearData);
            }
            if (line.contains("INSERT INTO `Teachers`")) {
                String clearData = line.replaceAll("INSERT INTO `Teachers` VALUES ", "");
                clearData = clearData.replaceAll(";", "");
                setTeacher(clearData);
            }
        }
            session.close();
            HibernateUtil.close();
    }

    private static void setStudent(String clearData) throws ParseException {


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
    }

    private static void setSubscription(String clearData) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        String[] fragments = clearData.split("\\),\\(");
        for (String frag : fragments) {
            String data = frag.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("'", "");
            String[] fields = data.split(",");
            session.getTransaction().begin();
            Subscription subscription = new Subscription();
            subscription.setId(new SubscriptionKey(Integer.parseInt(fields[0]), Integer.parseInt(fields[1])));
            subscription.setStudentId(Integer.parseInt(fields[0]));
            subscription.setCoursedId(Integer.parseInt(fields[1]));
            Date date = format.parse(fields[2]);
            subscription.setSubscriptionDate(date);

            session.save(subscription);
            session.getTransaction().commit();
        }
    }

    private static void setTeacher(String clearData) {
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

    }

    private static void parseAndSetCourse(String clearData) {
        String[] fragments = clearData.split("\\),\\(");
        for (String frag : fragments) {
            String data = frag.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("'", "");
            String[] fields = data.split(",");

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
        }
    }
}