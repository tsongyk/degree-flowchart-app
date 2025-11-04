package p466.degree_flowchart;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import p466.degree_flowchart.data.CourseRepository;
import p466.degree_flowchart.data.DegreeProgressRepository;
import p466.degree_flowchart.data.StudentRepository;
import p466.degree_flowchart.model.Course;
import p466.degree_flowchart.model.Student;
import p466.degree_flowchart.model.Course.Category;
import java.util.List;

@SpringBootApplication
public class DegreeFlowchartApplication {

	public static void main(String[] args) {
		SpringApplication.run(DegreeFlowchartApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(StudentRepository studentRepo, CourseRepository courseRepo, DegreeProgressRepository progressRepo) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				studentRepo.save(new Student(
					"1001",
					"password123",
					"John",
					"Doe",
					"jdoe@iu.edu",
					"Computer Science",
					"Software Engineering",
					"May 2026"
				));

				studentRepo.save(new Student(
					"1002",
					"password456",
					"Jane",
					"Smith",
					"jsmith@iu.edu",
					"Data Science",
					"Foundational Data Science",
					"December 2026"
				));

				Course c200 = new Course(
					"CSCI-C 200", 
					"Intro to Computers & Programming", 
					Category.CSCORE, 
					4
				);
				courseRepo.save(c200);

				Course c211 = new Course(
					"CSCI-C 211", 
					"Introduction to Computer Science", 
					Category.CSCORE, 
					4
				);
				courseRepo.save(c211);

				Course m211 = new Course(
					"MATH-M 211", 
					"Calculus I", 
					Category.MATH, 
					4
				);
				courseRepo.save(m211);

				Course eng170 = new Course(
					"ENG-W 170", 
					"Projects in Reading & Writing (Introduction to Argumentative Writing)", 
					Category.ELECTIVE, 
					3
				);
				courseRepo.save(eng170);

				Course c212 = new Course(
					"CSCI-C 212", 
					"Introduction to Software Systems", 
					Category.CSCORE, 
					4
				);
				c212.setPrerequisites(List.of(c211, c200));
				courseRepo.save(c212);

				Course m212 = new Course(
					"MATH-M 212", 
					"Calculus II", 
					Category.MATH, 
					4
				);
				m212.setPrerequisites(List.of(m211));
				courseRepo.save(m212);

				Course ealc111 = new Course(
					"EALC-E 111", 
					"War and Violence in East Asia", 
					Category.GENED, 
					3
				);
				courseRepo.save(ealc111);

				Course busA100 = new Course(
					"BUS-A 100", 
					"Introductory Accounting Principles and Analysis", 
					Category.ELECTIVE, 
					1
				);
				courseRepo.save(busA100);

				Course c241 = new Course(
					"CSCI-C 241", 
					"Discrete Structures for Computer Science", 
					Category.CSCORE, 
					4
				);
				c241.setPrerequisites(List.of(c200, c211));
				courseRepo.save(c241);

				Course c323 = new Course(
					"CSCI-C 323", 
					"Mobile App Development", 
					Category.CSE, 
					3
				);
				c323.setPrerequisites(List.of(c212));
				courseRepo.save(c323);

				Course folkE151 = new Course(
					"FOLK-E 151", 
					"Global Pop Music", 
					Category.GENED, 
					3
				);
				courseRepo.save(folkE151);

				Course aaaA295 = new Course(
					"AAAD-A 295", 
					"Survey of Hip Hop", 
					Category.GENED, 
					3
				);
				courseRepo.save(aaaA295);

				Course sphI150 = new Course(
					"SPH-I 150", 
					"Taekwondo", 
					Category.ELECTIVE, 
					1
				);
				courseRepo.save(sphI150);

				Course astA100 = new Course(
					"AST-A 100", 
					"The Solar System", 
					Category.ELECTIVE, 
					1
				);
				courseRepo.save(astA100);

				Course c343 = new Course(
					"CSCI-C 343", 
					"Data Structures", 
					Category.CSCORE, 
					4
				);
				c343.setPrerequisites(List.of(c212, c241));
				courseRepo.save(c343);

				Course c291 = new Course(
					"CSCI-C 291", 
					"System Programming With C and Linux", 
					Category.CSE, 
					3
				);
				c291.setPrerequisites(List.of(c200, c211));
				courseRepo.save(c291);

				Course y395 = new Course(
					"CSCI-Y 395", 
					"Career Development for CSCI Majors", 
					Category.CAREER, 
					1
				);
				courseRepo.save(y395);

				Course m303 = new Course(
					"MATH-M 303", 
					"Linear Algebra for Undergraduates", 
					Category.MATH, 
					4
				);
				m303.setPrerequisites(List.of(m212));
				courseRepo.save(m303);

				Course mus110 = new Course(
					"MUS-P 110", 
					"Beginning Piano Class I", 
					Category.GENED, 
					3
				);
				courseRepo.save(mus110);

				Course sphI111 = new Course(
					"SPH-I 111", 
					"Basketball", 
					Category.ELECTIVE, 
					1
				);
				courseRepo.save(sphI111);

				Course engL220 = new Course(
					"ENG-L 220", 
					"Intro to Prose", 
					Category.WRITING, 
					3
				);
				engL220.setPrerequisites(List.of(eng170));
				courseRepo.save(engL220);

				Course aaaD238 = new Course(
					"AAA-D 238", 
					"Communication in Black America", 
					Category.GENED, 
					3
				);
				courseRepo.save(aaaD238);

				Course psyP101 = new Course(
					"PSY-P 101", 
					"Intro to Psychology", 
					Category.GENED, 
					3
				);
				courseRepo.save(psyP101);

				Course psyP102 = new Course(
					"PSY-P 102", 
					"Intro to Psychology II", 
					Category.GENED, 
					3
				);
				psyP102.setPrerequisites(List.of(psyP101));
				courseRepo.save(psyP102);

				Course csciB403 = new Course(
					"CSCI-B 403", 
					"Intro to Algorithm Design and Analysis", 
					Category.CSE, 
					4
				);
				csciB403.setPrerequisites(List.of(c343));
				courseRepo.save(csciB403);

				Course csciB461 = new Course(
					"CSCI-B 461", 
					"Database Concepts", 
					Category.CSE, 
					4
				);
				csciB461.setPrerequisites(List.of(c241, c343));
				courseRepo.save(csciB461);

				Course csciB481 = new Course(
					"CSCI-B 481", 
					"Interactive Graphics", 
					Category.CSE, 
					4
				);
				csciB481.setPrerequisites(List.of(c212, c291, c343));
				courseRepo.save(csciB481);

				Course csciC335 = new Course(
					"CSCI-C 335", 
					"Computer Structures", 
					Category.CSE, 
					4
				);
				csciC335.setPrerequisites(List.of(c212, c291, c241));
				courseRepo.save(csciC335);

				Course csciP436 = new Course(
					"CSCI-P 436", 
					"Operating Systems", 
					Category.CSE, 
					4
				);
				csciP436.setPrerequisites(List.of(c343, csciC335));
				courseRepo.save(csciP436);

				Course csciP415 = new Course(
					"CSCI-P 415", 
					"Intro to Verification", 
					Category.CSE, 
					4
				);
				csciP415.setPrerequisites(List.of(c343, c241));
				courseRepo.save(csciP415);

				Course csciP465 = new Course(
					"CSCI-P 465", 
					"Software Engineering I", 
					Category.CSE, 
					4
				);
				csciP465.setPrerequisites(List.of(c343));
				courseRepo.save(csciP465);

				Course csciP466 = new Course(
					"CSCI-P 466", 
					"Software Engineering II", 
					Category.CSE, 
					4
				);
				csciP466.setPrerequisites(List.of(csciP465));
				courseRepo.save(csciP466);

				Course anthP200 = new Course(
					"ANTH-P 200", 
					"Introduction to Archaeology", 
					Category.GENED, 
					3
				);
				courseRepo.save(anthP200);

				Course csciC311 = new Course(
					"CSCI-C 311", 
					"Programming Languages", 
					Category.CSE, 
					4
				);
				csciC311.setPrerequisites(List.of(c241, c212));
				courseRepo.save(csciC311);

				Course csciC290 = new Course(
					"CSCI-C 290", 
					"Topics in Computing", 
					Category.CSE, 
					3
				);
				courseRepo.save(csciC290);

				Course csciB405 = new Course(
					"CSCI-B 405", 
					"Applied Algorithms", 
					Category.CSE, 
					4
				);
				csciB405.setPrerequisites(List.of(c343));
				courseRepo.save(csciB405);

				Course csciP424 = new Course(
					"CSCI-P 424", 
					"Advanced Functional Programming", 
					Category.CSE, 
					4
				);
				csciP424.setPrerequisites(List.of(csciC311));
				courseRepo.save(csciP424);

				Course csciB351 = new Course(
					"CSCI-B 351", 
					"Intro to AI", 
					Category.CSE, 
					4
				);
				csciB351.setPrerequisites(List.of(c200));
				courseRepo.save(csciB351);

				Course csciB441 = new Course(
					"CSCI-B 441", 
					"Digital Design", 
					Category.CSE, 
					4
				);
				csciB441.setPrerequisites(List.of(csciC335));
				courseRepo.save(csciB441);

				Course csciB453 = new Course(
					"CSCI-B 453", 
					"Game Development", 
					Category.CSE,
					4
				);
				csciB453.setPrerequisites(List.of(csciB481));
				courseRepo.save(csciB453);

				Course collC101 = new Course(
					"COLL-C 101", 
					"Intro to Chess", 
					Category.ELECTIVE,
					3
				);
				courseRepo.save(collC101);

				Course collX101 = new Course(
					"COLL-X 211", 
					"Intro to Chess (Experimental Topics)", 
					Category.ELECTIVE,
					3
				);
				collX101.setPrerequisites(List.of(collC101));
				courseRepo.save(collX101);


				Course anatA225 = new Course(
					"ANAT-A 225", 
					"Human Anatomy", 
					Category.GENED,
					3
				);
				courseRepo.save(anatA225);

				Course anthB200 = new Course(
					"ANTH-B 200", 
					"Bioanthropology", 
					Category.GENED,
					3
				);
				courseRepo.save(anthB200);

				Course astA221 = new Course(
					"AST-A 221", 
					"General Astronomy I", 
					Category.GENED,
					4
				);
				courseRepo.save(astA221);

				Course astA222 = new Course(
					"AST-A 222", 
					"General Astronomy II", 
					Category.GENED,
					4
				);
				astA222.setPrerequisites(List.of(astA221));
				courseRepo.save(astA222);

				Course biolB221 = new Course(
					"BIOL-B 221", 
					"Biology of Coffee", 
					Category.GENED,
					4
				);
				courseRepo.save(biolB221);

				Course mathM311 = new Course(
					"MATH-M 311", 
					"Calculus 3", 
					Category.MATH,
					4
				);
				mathM311.setPrerequisites(List.of(m212));
				courseRepo.save(mathM311);

				Course biolB376 = new Course(
					"BIOL-B 376", 
					"Biology of Birds", 
					Category.GENED,
					4
				);
				courseRepo.save(biolB376);

				Course chemC101 = new Course(
					"CHEM-C 101", 
					"Elementary Chemistry 1", 
					Category.GENED,
					3
				);
				courseRepo.save(chemC101);

				Course chemC102 = new Course(
					"CHEM-C 102", 
					"Elementary Chemistry 2", 
					Category.GENED,
					3
				);

				chemC102.setPrerequisites(List.of(chemC101));
				courseRepo.save(chemC102);

				Course physP221 = new Course(
					"PHYS-P 221", 
					"Physics 1", 
					Category.GENED,
					4
				);
				courseRepo.save(physP221);

				Course physP222 = new Course(
					"PHYS-P 222", 
					"Physics 2", 
					Category.GENED,
					4
				);

				physP222.setPrerequisites(List.of(physP221));
				courseRepo.save(physP222);

				Course amstA202 = new Course(
					"AMST-A 202", 
					"U.S. Arts and Media", 
					Category.ELECTIVE,
					4
				);
				courseRepo.save(amstA202);

				Course arthA160 = new Course(
					"ARTH-A 160", 
					"Intro to East Asian Art", 
					Category.ELECTIVE,
					4
				);
				courseRepo.save(arthA160);

				Course busM155 = new Course(
					"BUS-M 155", 
					"Topics in Marketing", 
					Category.ELECTIVE,
					4
				);
				courseRepo.save(busM155);

				Course mschA250 = new Course(
					"MSCH-A 250", 
					"Foundations of Advertising", 
					Category.ELECTIVE,
					4
				);
				courseRepo.save(mschA250);


				// Data Science courses:

				Course ilsZ410 = new Course(
					"ILS-Z 410", 
					"Social and Ethical Impacts of Big Data", 
					Category.ELECTIVE,
					3
				);
				courseRepo.save(ilsZ410);

				Course mathE201 = new Course(
					"MATH-E 201", 
					"Linear Algebra for Data Science", 
					Category.MATH,
					3
				);
				courseRepo.save(mathE201);

				Course mathE265 = new Course(
					"MATH-E 265", 
					"Probability for Data Science", 
					Category.MATH,
					3
				);
				courseRepo.save(mathE265);

				Course csciA310 = new Course(
					"CSCI-A 310", 
					"Probability for Data Science", 
					Category.DSCORE,
					3
				);
				courseRepo.save(csciA310);

				Course dsciD321 = new Course(
					"DSCI-D 321", 
					"Data Representation and Processing", 
					Category.DSCORE,
					3
				);
				courseRepo.save(dsciD321);

				Course dsciD351 = new Course(
					"DSCI-D 351", 
					"Big Data Analytics", 
					Category.DSCORE,
					3
				);
				courseRepo.save(dsciD351);

				Course infoI123 = new Course(
					"INFO-I 123", 
					"Data Fluency", 
					Category.DSCORE,
					3
				);
				courseRepo.save(infoI123);

				Course statS350 = new Course(
					"STAT-S 350", 
					"Intro to Statistical Inference", 
					Category.DSCORE,
					3
				);
				statS350.setPrerequisites(List.of(infoI123));
				courseRepo.save(statS350);

				Course statS352 = new Course(
					"STAT-S 352", 
					"Data Modeling and Inference", 
					Category.DSCORE,
					3
				);
				statS352.setPrerequisites(List.of(statS350));
				courseRepo.save(statS352);

				Course dsciD498 = new Course(
					"DSCI-D 498", 
					"Data Science Capstone I", 
					Category.DSCORE,
					3
				);
				dsciD498.setPrerequisites(List.of(dsciD351, statS352));
				courseRepo.save(dsciD498);

				Course dsciD499 = new Course(
					"DSCI-D 499", 
					"Data Science Capstone II", 
					Category.DSCORE,
					3
				);
				dsciD499.setPrerequisites(List.of(dsciD498));
				courseRepo.save(dsciD499);

				Course csciB455 = new Course(
					"CSCI-B 455", 
					"Principles of Machine Learning", 
					Category.DSE,
					3
				);
				csciB455.setPrerequisites(List.of(csciB351, infoI123));
				courseRepo.save(csciB455);


			}
		};
	}
}