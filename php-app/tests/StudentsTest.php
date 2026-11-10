<?php

use PHPUnit\Framework\TestCase;

require_once __DIR__ . '/../Students.php';

class StudentsTest extends TestCase
{
    private PDO $pdo;

    protected function setUp(): void
    {
        $this->pdo = Students::connect();
        $this->pdo->exec('TRUNCATE students RESTART IDENTITY');
    }

    public function testAjoutEtLecture(): void
    {
        $id = Students::addStudent($this->pdo, 'Amel', 15);
        $student = Students::getStudent($this->pdo, $id);
        $this->assertSame('Amel', $student['name']);
        $this->assertSame(15.0, $student['grade']);
    }

    public function testEtudiantInexistant(): void
    {
        $this->assertNull(Students::getStudent($this->pdo, 999));
    }

    public function testMoyenne(): void
    {
        Students::addStudent($this->pdo, 'Amel', 10);
        Students::addStudent($this->pdo, 'Karim', 14);
        $this->assertSame(12.0, Students::averageGrade($this->pdo));
    }
}
