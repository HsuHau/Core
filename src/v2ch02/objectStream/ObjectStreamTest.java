package v2ch02.objectStream;/*
package objectStream;

import java.io.*;

public class ObjectStreamTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Employee harry = new Employee("Carl Cracker", 75000, 1987, 12, 15);
        Employee carl = new Employee("Harry Hacker", 50000, 1989, 10, 1);
        carl.setSecretary(harry);
        Manage tony = new Employee("Tony Tester", 40000, 1990, 3, 15);
        tony.setSecretary(harry);

        Employee[] staff = new Employee[3];

        staff[0] = carl;
        staff[1] = harry;
        staff[2] = tony;

        // save all employee records to file employee.dat
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("D://employee.dat"))) {
            out.writeObject(staff);
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("D://employee.dat"))) {
            // retrieve all records into a new array

            Employee[] newStaff = (Employee[]) in.readObject();

            // raise secretary's salary
            newStaff[1].raiseSalary(10);

            // print the newly read employee records
            for (Employee e : newStaff) {
                System.out.println(e);
            }
        }
    }
}
*/
