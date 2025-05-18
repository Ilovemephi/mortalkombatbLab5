
package mephi.b22901.ae.lab5;

     public class EnemyFactory {
    public static Player createEnemy(EnemyType type, int level) {
        switch (type) {
            case BARAKA:
                return new Baraka(level, 100, 12, 1);
            case SUBZERO:
                return new SubZero(level, 60, 16, 1);
            case LIUKANG:
                return new LiuKang(level, 70, 20, 1);
            case SONYABLADE:
                return new SonyaBlade(level, 80, 16, 1);
            case SHAOKAHN:
                // Для босса разные параметры в зависимости от уровня
                if (level == 3) {
                    return new ShaoKahn(level, 100, 30, 1);
                } else {
                    return new ShaoKahn(level, 145, 44, 1);
                }
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + type);
        }
    }
}
