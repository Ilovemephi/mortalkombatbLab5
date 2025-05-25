
package mephi.b22901.ae.lab5;

     public class EnemyFactory {
         /**
          * Создаем соперников
          */
    public static Player createEnemy(EnemyType type, int level) {
        switch (type) {
            case BARAKA:
                return new Baraka(level, 90, 12, 1);
            case SUBZERO:
                return new SubZero(level, 60, 14, 1);
            case LIUKANG:
                return new LiuKang(level, 70, 17, 1);
            case SONYABLADE:
                return new SonyaBlade(level, 80, 13, 1);
            case SHAOKAHN:
                    return new ShaoKahn(level, 150, 40, 1);
                
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + type);
        }
    }
}
