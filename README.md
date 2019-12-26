# java-doctor
A utility for reading JavaDocs from a URL in a pseudo-high level fashion.

-----

#### Example from [JavaDoctorExample](https://github.com/Sxtanna/java-doctor/tree/master/src/test/java/com/sxtanna/jdoctor/JavaDoctorExample.java)

```java
final String url = "https://papermc.io/javadocs/paper/1.15/index-all.html";
// usages of the make function allow the application to continue in the current context without interruption due to IO
try (final JavaDoctor doctor = JavaDoctor.make(new URL(url)))
{
	doctor.fetch();
	final Optional<JavaDoctorObject> type = doctor.findType("Barrel");
	final Optional<JavaDoctorObject> desc = doctor.findDesc(type);
	System.out.println("Description of Barrel Interface: " + desc.flatMap(JavaDoctorObject::text).orElse("Unknown"));
}
catch (Exception ex)
{
	ex.printStackTrace();
}

// usages of the exam function cause the doctor to immediately fetch the html document
// benefits being that by the time the context continues, the doctor can be used directly.
try (final JavaDoctor doctor = JavaDoctor.exam(new URL(url)))
{
	final Optional<JavaDoctorObject> type = doctor.findType("BatToggleSleepEvent");
	final Optional<JavaDoctorObject> desc = doctor.findDesc(type);
	System.out.println("Description of BatToggleSleepEvent Class: " + desc.flatMap(JavaDoctorObject::text).orElse("Unknown"));
}
catch (Exception ex)
{
	ex.printStackTrace();
}
```