package enigma;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Wajahat Khan
 */
class MovingRotor extends Rotor {
    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */

    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        this._notches = notches;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    String notches() {
        return _notches;
    }

    @Override
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i++) {
            int notch = alphabet().toInt(_notches.charAt(i));
            if (setting() == notch) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        this.set(super.permutation().wrap(this.setting() + 1));
    }

    /**Variable of notches.*/
    private String _notches;
}


