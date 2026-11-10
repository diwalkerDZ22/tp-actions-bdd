import pytest

from students import get_connection, add_student, get_student, average_grade


@pytest.fixture
def conn():
    """Connexion à la base ; la table est vidée avant chaque test."""
    connection = get_connection()
    with connection.cursor() as cur:
        cur.execute("TRUNCATE students RESTART IDENTITY")
    connection.commit()
    yield connection
    connection.close()


def test_ajout_et_lecture(conn):
    student_id = add_student(conn, "Amel", 15)
    student = get_student(conn, student_id)
    assert student["name"] == "Amel"
    assert student["grade"] == 15.0


def test_etudiant_inexistant(conn):
    assert get_student(conn, 999) is None


def test_moyenne(conn):
    add_student(conn, "Amel", 10)   # 1. Préparer : deux étudiants
    add_student(conn, "Karim", 14)
    assert average_grade(conn) == 12.0  # 2. Agir et 3. Vérifier : (10 + 14) / 2 = 12
