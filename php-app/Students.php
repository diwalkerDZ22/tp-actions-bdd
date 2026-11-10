<?php

class Students
{
    public static function connect(): PDO
    {
        return new PDO(
            'pgsql:host=localhost;port=5432;dbname=tp',
            'postgres',
            'postgres',
            [PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION]
        );
    }

    public static function addStudent(PDO $pdo, string $name, float $grade): int
    {
        $stmt = $pdo->prepare(
            'INSERT INTO students (name, grade) VALUES (:name, :grade) RETURNING id'
        );
        $stmt->execute([':name' => $name, ':grade' => $grade]);
        return (int) $stmt->fetchColumn();
    }

    public static function getStudent(PDO $pdo, int $id): ?array
    {
        $stmt = $pdo->prepare('SELECT id, name, grade FROM students WHERE id = :id');
        $stmt->execute([':id' => $id]);
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        if ($row === false) {
            return null;
        }
        return [
            'id' => (int) $row['id'],
            'name' => $row['name'],
            'grade' => (float) $row['grade'],
        ];
    }

    public static function averageGrade(PDO $pdo): ?float
    {
        $value = $pdo->query('SELECT AVG(grade) FROM students')->fetchColumn();
        return $value === null ? null : (float) $value;
    }
}
