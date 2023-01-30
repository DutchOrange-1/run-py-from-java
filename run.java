    public String run_Python_File() {
        String stages = "no Data"; 
        String pyPath = "Python Path";
        try {
            ProcessBuilder pyb = new ProcessBuilder(pyPath, "./pythonScripts/eskom.py"); // Can add input stuff in last
                                                                                         // slot
            pyb.redirectErrorStream(true);
            Process process = pyb.start();
            InputStream out = process.getInputStream();
            OutputStream in = process.getOutputStream();

            // Where the data is sotored from the input stream
            // This section of code is from:
            // https://gist.github.com/pditommaso/2265496?permalink_comment_id=1794643
            // and edited for this purpose
            byte[] buffer = new byte[4000];
            while (process.isAlive()) {
                int no = out.available();
                if (no > 0) {
                    int n = out.read(buffer, 0, Math.min(no, buffer.length));
                    // stages = (new String(buffer, 0, n));
                    System.out.println(new String(buffer, 0, n));
                }

                // Checking if there is any data on the input stream
                int ni = System.in.available();
                // IF there is any data it reads up to the lower part of ni
                if (ni > 0) {
                    int n = System.in.read(buffer, 0, Math.min(ni, buffer.length));
                    in.write(buffer, 0, n);
                    in.flush();
                }
            }
            // Prints out the data from exit() in the py script
            System.out.println("Exit code: " + process.exitValue());

        } catch (Exception e) {
            System.out.println("An exception occured: " + e);
            e.printStackTrace();
        }

        return stages;
    }
