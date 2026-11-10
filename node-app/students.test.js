const { createPool, addStudent, getStudent, averageGrade } = require("./students");

let pool;

beforeAll(() => {
  pool = createPool();
});

beforeEach(async () => {
  await pool.query("TRUNCATE students RESTART IDENTITY");
});

afterAll(async () => {
  await pool.end();
});

test("ajouter un étudiant puis le relire", async () => {
  const id = await addStudent(pool, "Amel", 15);
  const student = await getStudent(pool, id);
  expect(student.name).toBe("Amel");
  expect(student.grade).toBe(15);
});

test("lire un étudiant inexistant", async () => {
  expect(await getStudent(pool, 999)).toBeNull();
});

test("moyenne des notes", async () => {
  await addStudent(pool, "Amel", 10);
  await addStudent(pool, "Karim", 14);
  expect(await averageGrade(pool)).toBe(12);
});
