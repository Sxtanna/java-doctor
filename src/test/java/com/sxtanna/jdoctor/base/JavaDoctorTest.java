package com.sxtanna.jdoctor.base;

import com.sxtanna.jdoctor.JavaDoctor;
import com.sxtanna.jdoctor.JavaDoctorObject;
import org.junit.jupiter.api.*;

import java.net.URL;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class JavaDoctorTest
{

	private final AtomicReference<JavaDoctor> doctor = new AtomicReference<>();


	protected abstract String url();


	protected abstract String case0Type();

	protected abstract String case0Desc();


	protected abstract String case1Type();

	protected abstract String case1Desc();


	@BeforeAll
	void setUp()
	{
		assertDoesNotThrow(() -> doctor.set(JavaDoctor.make(new URL(url()))),
						   "Failed to make JavaDoctor");

		assertDoesNotThrow(() -> doctor.get().fetch(),
						   "Failed to fetch Docs");
	}

	@AfterAll
	void tearDown()
	{
		assertDoesNotThrow(() -> doctor.getAndSet(null).close(),
						   "Failed to close JavaDoctor");
	}

	@Test
	@Order(0)
	void testCase0()
	{
		final JavaDoctor doctor = this.doctor.get();
		assertNotNull(doctor,
					  "Doctor should not be null!");

		final Optional<JavaDoctorObject> type = doctor.findType(case0Type());
		final Optional<JavaDoctorObject> desc = doctor.findDesc(type);

		assertEquals(Optional.of(case0Type()), type.flatMap(JavaDoctorObject::text));
		assertEquals(Optional.of(case0Desc()), desc.flatMap(JavaDoctorObject::text));
	}

	@Test
	@Order(1)
	void testCase1()
	{
		final JavaDoctor doctor = this.doctor.get();
		assertNotNull(doctor,
					  "Doctor should not be null!");

		final Optional<JavaDoctorObject> type = doctor.findType(case1Type());
		final Optional<JavaDoctorObject> desc = doctor.findDesc(type);

		assertEquals(Optional.of(case1Type()), type.flatMap(JavaDoctorObject::text));
		assertEquals(Optional.of(case1Desc()), desc.flatMap(JavaDoctorObject::text));
	}

}
