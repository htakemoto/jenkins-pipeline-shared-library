package org.example

/**
 * Description: This provides basic function with class
 *
 * Usage:
 *
 *   mock.checkout()
 *   mock.compile()
 *   mock.deploy()
 *   mock.test()
 *
 */

class Mock implements Serializable {
    private final script

    Mock(script) {
        this.script = script
    }

    def checkout() {
        this.script.stage("checkout") {
            this.script.sh "ls -l"
        }
    }

    def compile() {
        this.script.stage("compile") {
            this.script.echo "compile"
        }
    }

    def deploy() {
        this.script.stage("deploy") {
            this.script.echo "deploy"
        }
    }

    def test() {
        this.script.stage("test") {
            this.script.echo "test"
        }
    }
}