import Container from "./components/Container";
import Box from "./components/Box";
import Header from "./components/Header";
import RegEmail from "./components/Email";
import Send from "./components/Send";
import Code from "./components/Code";
import Password from "./components/Password";
import Signup from "./components/Submit";
import Footer from "./components/Footer";

import "./App.css";

function App() {
  return (
    <div className="App">
      <Container>
        <Box>
          <Header />
          <RegEmail>
            <Send />
          </RegEmail>
          <Code />
          <Password />
          <Signup />
          <Footer />
        </Box>
      </Container>
    </div>
  );
}

export default App;
