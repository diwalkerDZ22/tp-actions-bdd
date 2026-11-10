import psycopg2


def get_connection():
    """Ouvre une connexion à la base tp (localhost, postgres/postgres)."""
    return psycopg2.connect(
        host="localhost", port=5432, dbname="tp",
        user="postgres", password="postgres",
    )


def add_student(conn, name, grade):
    """Ajoute un étudiant et retourne son id."""
    with conn.cursor() as cur:
        cur.execute(
            "INSERT INTO students (name, grade) VALUES (%s, %s) RETURNING id",
            (name, grade),
        )
        student_id = cur.fetchone()[0]
    conn.commit()
    return student_id


def get_student(conn, student_id):
    """Retourne un dict {id, name, grade} ou None si l'étudiant n'existe pas."""
    with conn.cursor() as cur:
        cur.execute(
            "SELECT id, name, grade FROM students WHERE id = %s",
            (student_id,),
        )
        row = cur.fetchone()
    if row is None:
        return None
    return {"id": row[0], "name": row[1], "grade": float(row[2])}


def average_grade(conn):
    with conn.cursor() as cur:
        cur.execute("SELECT AVG(grade) FROM students")
        result = cur.fetchone()[0]
    return float(result) if result is not None else None
