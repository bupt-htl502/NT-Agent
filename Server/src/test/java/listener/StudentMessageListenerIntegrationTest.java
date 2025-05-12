package listener;

import com.coldwindx.server.ServerApplication;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.listener.StudentMessageListener;
import com.coldwindx.server.manager.MinioMananger;
import com.coldwindx.server.service.impl.StudentServiceImpl;
import org.checkerframework.checker.signature.qual.PolySignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ServerApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentMessageListenerIntegrationTest {
    @Autowired
    private StudentMessageListener studentMessageListener;
    @Autowired
    private StudentServiceImpl studentServiceImpl;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    public void testRegistration() throws InterruptedException {
        Student student = new Student();
        student.setId(1L);
        student.setStudentNo("1");
        student.setName("name1");
        student.setRole(100);
        student.setGrade(0);
//        studentServiceImpl.insert(student);
        rabbitTemplate.convertAndSend("ex_student", "registed_test", student);
//        System.out.println("12345");
        student.setId(2L);
        rabbitTemplate.convertAndSend("ex_student", "registed_test", student);
        student.setId(3L);
        rabbitTemplate.convertAndSend("ex_student", "registed_test", student);
        student.setId(4L);
        rabbitTemplate.convertAndSend("ex_student", "registed_test", student);
        student.setId(5L);
        rabbitTemplate.convertAndSend("ex_student", "registed_test", student);
        student.setId(6L);
        rabbitTemplate.convertAndSend("ex_student", "registed_test", student);
//        Thread.sleep(10000);
//        studentServiceImpl.insert(student);
        System.out.println("Main thread done sending.");
//        studentMessageListener.registration(student);
    }

}
