package listener;

import com.coldwindx.server.ServerApplication;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.listener.StudentMessageListener;
import com.coldwindx.server.manager.MinioMananger;
import com.coldwindx.server.service.impl.StudentServiceImpl;
import org.checkerframework.checker.signature.qual.PolySignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ServerApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentMessageListenerIntegrationTest {
    @Autowired
    private StudentMessageListener studentMessageListener;

    @Test
    public void testRegistration() {
        Student student = new Student();
        student.setId(123213L);
        student.setStudentNo("1234567");
        student.setName("name1");
        student.setRole(100);
        student.setGrade(0);
        studentMessageListener.registration(student);
    }

}
