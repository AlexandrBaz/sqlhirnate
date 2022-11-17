package keys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SubscriptionKey implements Serializable {
    @Column(name = "student_id", insertable = false, updatable = false)
    public int studentId;
    @Column(name = "course_id", insertable = false, updatable = false)
    public int courseID;

    public SubscriptionKey(){}

    public SubscriptionKey(int studentId, int courseID) {
        this.studentId = studentId;
        this.courseID = courseID;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionKey that = (SubscriptionKey) o;
        return studentId == that.studentId && courseID == that.courseID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseID);
    }
}
