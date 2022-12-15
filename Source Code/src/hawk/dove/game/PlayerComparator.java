/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hawk.dove.game;

import java.util.Comparator;

/**
 *
 * @author moust
 */
public class PlayerComparator implements Comparator<PlayerAgent> {
    @Override
    public int compare(PlayerAgent p1, PlayerAgent p2) {
                if (p1.getPayOff() < p2.getPayOff())
                    return 1;
                else if (p1.getPayOff() > p2.getPayOff())
                    return -1;
                return 0;
            }
}
